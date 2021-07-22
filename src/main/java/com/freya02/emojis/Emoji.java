package com.freya02.emojis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Emoji {
	private final String subpage;
	private final String name;
	private final String unicode;
	private final List<String> shortcodes; //Shortcodes does NOT have : hee

	public Emoji(String subpage, String name, String unicode, List<String> shortcodes) {
		this.subpage = subpage;
		this.name = name;
		this.unicode = unicode;
		this.shortcodes = shortcodes;

		for (String shortcode : shortcodes) {
			if (shortcode.contains(":")) {
				throw new IllegalArgumentException(": not allowed in shortcode " + shortcode);
			}
		}
	}

	public String subpage() { return subpage; }

	public String name() { return name; }

	public String unicode() { return unicode; }

	public List<String> shortcodes() { return shortcodes; }

	public List<String> getUTF16() {
		int[] codepoints = unicode.codePoints().toArray();

		List<String> list = new ArrayList<>();
		for (int codePoint : codepoints) {
			final char[] chars = Character.toChars(codePoint);
			for (char aChar : chars) {
				final String hex = Integer.toHexString(aChar).toUpperCase();
				if (hex.equals("FE0F")) continue; //Skip invisible codepoint which specifies that the preceding character should be displayed with emoji presentation

				list.add("\\u" + "0".repeat(4 - hex.length()) + hex);
			}
		}

		return list;
	}

	public List<String> getUnicodeCodepoints() {
		return unicode.codePoints()
				.mapToObj(codepoint -> "U+" + Integer.toHexString(codepoint).toUpperCase())
				.collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Emoji emoji = (Emoji) o;

		if (!subpage.equals(emoji.subpage)) return false;
		if (!name.equals(emoji.name)) return false;
		if (!unicode.equals(emoji.unicode)) return false;
		return shortcodes.equals(emoji.shortcodes);
	}

	@Override
	public int hashCode() {
		Objects.hash(subpage, name, unicode, shortcodes);
		int result = subpage.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + unicode.hashCode();
		result = 31 * result + shortcodes.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Emoji{" +
				"subpage='" + subpage + '\'' +
				", name='" + name + '\'' +
				", unicode='" + unicode + '\'' +
				", shortcodes=" + shortcodes +
				'}';
	}
}