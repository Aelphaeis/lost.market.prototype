package com.cruat.lost.ark.market;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

/**
 * This class enumerators over top level windows and checks to see whether or
 * not the process name matches a specific REGEX pattern. If it does, this class
 * returns an object representing the handle of that window.
 * 
 * @author Aelphaeis
 *
 */
public class ProcessNameResolver {

	private final Pattern pattern;

	public ProcessNameResolver(Pattern pattern) {
		this.pattern = Objects.requireNonNull(pattern);
	}

	public Optional<HWND> getFirstHandle() {
		ResolverCallback callback = new ResolverCallback();
		User32.INSTANCE.EnumWindows(callback, null);

		Map<String, HWND> filtered = callback.getHandleMap()
				.entrySet()
				.stream()
				.filter(p -> pattern.asPredicate().test(p.getKey()))
				.limit(1)
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

		if (filtered.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(filtered.values().iterator().next());

	}

	private class ResolverCallback implements WNDENUMPROC {

		private final Map<String, HWND> handleMap;

		public ResolverCallback() {
			this.handleMap = new HashMap<>();
		}

		@Override
		public boolean callback(HWND hWnd, Pointer data) {
			String windowTitle = getWindowText(hWnd);
			handleMap.put(windowTitle, hWnd);
			return true;
		}

		private String getWindowText(HWND hWnd) {
			char[] windowTextChars = new char[512];
			User32.INSTANCE.GetWindowText(hWnd, windowTextChars, 512);
			return new String(windowTextChars).trim();
		}

		public Map<String, HWND> getHandleMap() {
			return Collections.unmodifiableMap(this.handleMap);
		}
	}
}
