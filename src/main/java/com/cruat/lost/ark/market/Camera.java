package com.cruat.lost.ark.market;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Objects;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

public class Camera {

	private final HWND handle;

	public Camera(HWND handle) {
		this.handle = Objects.requireNonNull(handle);
	}

	public BufferedImage capture() {
		return capture(getCaptureRegion());
	}

	public BufferedImage capture(Rectangle rect) {
		return null;
	}

	public Rectangle getCaptureRegion() {
		RECT winRect = new RECT();
		User32.INSTANCE.GetWindowRect(handle, winRect);
		Rectangle region = new Rectangle();

		// Normalize this for when the game is not on main monitor
		region.height = winRect.bottom - winRect.top;
		region.width = winRect.right - winRect.left;
		region.x = 0;
		region.y = 0;

		return region;
	}
}
