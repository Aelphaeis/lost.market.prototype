package com.cruat.lost.ark.market;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String... args) {
		// TODO improve this REGEX
		Pattern regex = Pattern.compile("LOST ARK");

		// Lets resolve our Handle
		HWND handle = new ProcessNameResolver(regex)
				.getFirstHandle()
				.orElseThrow(PrototypeException::new);

		Camera camera = new Camera(handle);
		show(camera.capture());
	}

	public static void show(BufferedImage image) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}
