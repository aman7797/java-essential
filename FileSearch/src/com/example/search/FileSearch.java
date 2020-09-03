package com.example.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileSearch {

	String path;
	String regex;
	String zipFileName;
	Pattern pattern;
	List<File> zipFile = new ArrayList<File>();
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	public String getZipFileName() {
		return zipFileName;
	}

	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}

	public static void main(String[] args) {
		FileSearch app = new FileSearch();
		switch(Math.min(args.length, 3)){
		case 0: System.out.println("USASGE: Filesearch path [regex] [zipFileName]");
		case 3: app.setZipFileName(args[2]);
		case 2: app.setRegex(args[1]);
		case 1: app.setPath(args[0]);
		
		}
		try{
			app.walkDirectory(app.getPath());
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private void walkDirectory(String path) throws IOException {
		System.out.println("walkDirectory: " + path);
		Files.walk(Paths.get(path)).forEach(f -> processFile(f.toFile()));
	}

	private Object processFile(File file) {
//		System.out.println("processFile :"+file);
		try {
			if(searchFile(file)){
				addFileToZip(file);
			}
		} catch (IOException | UncheckedIOException e) {
			System.out.println("Processing Exception: " + e);
		}
		return null;
	}

	private void addFileToZip(File file) {
		if(getZipFileName()!=null){
			zipFile.add(file);
		}
	}

	private boolean searchFile(File file) throws IOException {
		return Files.lines(file.toPath(),StandardCharsets.UTF_8)
				.anyMatch(t->searchText(t));
	}

	private boolean searchText(String text) {
		return (this.getRegex()==null) ? true :
			text.matches(this.getRegex());
	}

}
