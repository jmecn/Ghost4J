package net.jmecn.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.ghost4j.analyzer.AnalysisItem;
import org.ghost4j.analyzer.FontAnalyzer;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.pdf.PdfReader;

public class TestPdf2Png {

	private static Logger logger = LoggerFactory.getLogger(TestPdf2Png.class);

	public final static String FILE_NAME = "input.pdf";

	@Test
	public void testPdfReader() {
		try {
			// load pdf with itextpdf
			PdfReader reader = new PdfReader(new FileInputStream(FILE_NAME));
			assertTrue("", reader.getNumberOfPages() == 11);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPdfDocument() {
		try {
			// load pdf with ghost4j
			PDFDocument document = new PDFDocument();
			document.load(new File(FILE_NAME));

			assertTrue(document.getPageCount() == 11);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	@Test public void testPdfPageToImage() {
		try {
			PDFDocument document = new PDFDocument();
			document.load(new File(FILE_NAME));

			// create renderer
			SimpleRenderer renderer = new SimpleRenderer();
			// set resolution (in DPI)
			renderer.setResolution(300);

			// render the first page
			List<Image> images = renderer.render(document, 0, 0);
			
			// write images to files to disk as PNG
		    try {
				for (int i = 0; i < images.size(); i++) {
				    ImageIO.write((RenderedImage) images.get(i), "png", new File("output/" + (i + 1) + ".png"));
				}
		    } catch (IOException e) {
		    	logger.error("ERROR: {}", e.getMessage(), e);
		    }
		} catch (Exception e) {
			logger.error("ERROR: {}", e.getMessage(), e);
		}
	}

	@Test public void testPdfToImage() {
		try {
			PDFDocument document = new PDFDocument();
			document.load(new File(FILE_NAME));

			// create renderer
			SimpleRenderer renderer = new SimpleRenderer();
			// set resolution (in DPI)
			renderer.setResolution(300);

			// render
			List<Image> images = renderer.render(document);
			
			// write images to files to disk as PNG
		    try {
				for (int i = 0; i < images.size(); i++) {
				    ImageIO.write((RenderedImage) images.get(i), "png", new File("output/" + (i + 1) + ".png"));
				}
		    } catch (IOException e) {
		    	logger.error("ERROR: {}", e.getMessage(), e);
		    }
		} catch (Exception e) {
			logger.error("ERROR: {}", e.getMessage(), e);
		}
	}
	
	@Test public void testPdfFontAnalysis() {
		try {

		    // load PDF document
		    PDFDocument document = new PDFDocument();
		    document.load(new File(FILE_NAME));

		    // create analyzer
		    FontAnalyzer analyzer = new FontAnalyzer();

		    // analyze
		    List<AnalysisItem> fonts = analyzer.analyze(document);

		    // print result
		    for (AnalysisItem analysisItem : fonts) {
		    	logger.info("{}", analysisItem);
		    }

		} catch (Exception e) {
		    logger.error("ERROR: {}", e.getMessage(), e);
		}
	}
}
