package com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt;

import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.common.TimedLine;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.common.TimedTextFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class <SRTLine> represents an SRT file, meandin a complete set of subtitle lines
 *
 */
public class SRTSub implements TimedTextFile {

	private static final long serialVersionUID = -2909833999376537734L;

	private String fileName;
	private Set<SRTLine> lines = new TreeSet<>();
	private List<SRTLine> lineList;

	// ======================== Public methods ==========================

	public void add(SRTLine line) {
		
		this.lines.add(line);
	}

	public void remove(TimedLine line) {
		
		this.lines.remove(line);
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for (SRTLine srtLine : lines) {
			sb.append(srtLine);
		}
		return sb.toString();
	}

	// ===================== getter and setter start =====================

	public Set<SRTLine> getLines() {
		return this.lines;
	}

	@Override
	public Set<? extends TimedLine> getTimedLines() {
		return this.lines;
	}

	public void setLines(Set<SRTLine> lines) {
		this.lines = lines;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLength(){
		if(lineList == null){
			lineList = new ArrayList<>(lines);
		}

		return lineList.size();
	}

	public SRTLine getSrtLine(int position){

		if(lineList == null){
			lineList = new ArrayList<>(lines);
		}

		return lineList.get(position);
	}

}
