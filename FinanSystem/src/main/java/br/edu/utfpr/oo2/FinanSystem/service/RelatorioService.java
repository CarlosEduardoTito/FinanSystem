package br.edu.utfpr.oo2.FinanSystem.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import br.edu.utfpr.oo2.FinanSystem.entities.Categoria;
import br.edu.utfpr.oo2.FinanSystem.entities.Conta;
import br.edu.utfpr.oo2.FinanSystem.entities.Transacao;

public class RelatorioService {

	private final TransacaoService transacaoService = new TransacaoService();
	private final CategoriaService categoriaService = new CategoriaService();
	private final ContaService contaService = new ContaService();
	
	public List<Transacao> buscarTransacoesMensais(int mes, int ano) throws SQLException, IOException{
		
		LocalDate inicio = LocalDate.of(ano, mes, 1);
		LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());
		
		return transacaoService.listarPorPeriodo(inicio, fim);
		
	}
	
	public void exportarPdf(List<Transacao> transacoes, String caminhoArquivo) throws Exception {
		
		Map<Integer, String> mapaCategorias = carregarMapaCategorias();
		Map<Integer, String> mapaContas = carregarMapaContas();
		
		PdfWriter writer = new PdfWriter(caminhoArquivo);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);
		
		document.add(new Paragraph("Relatório Financeiro Mensal").setBold().setFontSize(18));
		document.add(new Paragraph("Gerado em: " + LocalDate.now()));
		
		Table table = new Table(UnitValue.createPercentArray(new float[] {15, 20, 20, 25, 20}));
		table.setWidth(UnitValue.createPercentValue(100));
		
		table.addHeaderCell("Data");
		table.addHeaderCell("Conta");
		table.addHeaderCell("Categoria");
		table.addHeaderCell("Descrição");
		table.addHeaderCell("Valor (R$)");
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		double total = 0;
		
		for(Transacao t : transacoes) {
			
			table.addCell(t.getData().format(fmt));
			table.addCell(mapaContas.getOrDefault(t.getContaId(), "ID: " + t.getContaId()));
			table.addCell(mapaCategorias.getOrDefault(t.getCategoriaId(), "ID: " + t.getCategoriaId()));
			table.addCell(t.getDescricao());
			table.addCell(String.format("%.2f", t.getValor()));
			
			total += t.getValor();
		}
		
		document.add(table);
		document.add(new Paragraph("\nTotal movimentado (Absoluto): R$ " + String.format("%.2f", total)));
		
		document.close();	
	}
	
	public void exportarExcel(List<Transacao> transacoes, String caminhoArquivo) throws Exception{
		
		Map<Integer, String> mapaCategorias = carregarMapaCategorias();
		Map<Integer, String> mapaContas = carregarMapaContas();
		
		try (Workbook workbook = new XSSFWorkbook()){
			
			Sheet sheet = workbook.createSheet("Relatório");
			
			Row headerRow = sheet.createRow(0);
			String[] colunas = {"ID", "Data", "Conta", "Categoria", "Descrição", "Valor"};
			
			for(int i = 0; i < colunas.length; i++) {
				
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(colunas[i]);
				
			}
			
			int rowNum = 1;
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					
			for(Transacao t : transacoes) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(t.getId());
				row.createCell(1).setCellValue(t.getData().format(fmt));
				row.createCell(2).setCellValue(mapaContas.getOrDefault(t.getContaId(), "ID " + t.getContaId()));
				row.createCell(3).setCellValue(mapaCategorias.getOrDefault(t.getCategoriaId(), "ID " + t.getCategoriaId()));
				row.createCell(4).setCellValue(t.getDescricao());
				row.createCell(5).setCellValue(t.getValor());
			}
			
			for (int i = 0; i < colunas.length; i++) {
				
				sheet.autoSizeColumn(i);
				
			}
			
			try(FileOutputStream fileOut = new FileOutputStream(caminhoArquivo)){
				
				workbook.write(fileOut);
				
			}
		}
		
	}
	
	public Map<String, Double> gerarRelatorioAnualAgrupado(int ano) throws Exception{
		
		LocalDate inicio = LocalDate.of(ano, 1, 1);
		LocalDate fim = LocalDate.of(ano, 12, 31);
		
		List<Transacao> transacoesAno = transacaoService.listarPorPeriodo(inicio, fim);
		Map<Integer, String> mapaCategorias = carregarMapaCategorias();
		
		Map<String, Double> resumo = new HashMap<>();
		
		for(Transacao t : transacoesAno) {
			
			String nomeCategoria = mapaCategorias.getOrDefault(t.getCategoriaId(), "Outros");
			
			resumo.put(nomeCategoria, resumo.getOrDefault(nomeCategoria, 0.0) + t.getValor());
			
		}
		
		return resumo;
		
	}
	
	public void exportarPdfAnual(Map<String, Double> dadosAgrupados, int ano, String caminhoArquivo) throws Exception {
        PdfWriter writer = new PdfWriter(caminhoArquivo);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Relatório Anual Consolidado - " + ano).setBold().setFontSize(18));
        
        Table table = new Table(UnitValue.createPercentArray(new float[]{70, 30}));
        table.setWidth(UnitValue.createPercentValue(100));
        
        table.addHeaderCell("Categoria");
        table.addHeaderCell("Total (R$)");
        
        double saldoTotal = 0;

        for (Map.Entry<String, Double> entry : dadosAgrupados.entrySet()) {
            table.addCell(entry.getKey());
            table.addCell(String.format("R$ %.2f", entry.getValue()));
            
            saldoTotal += entry.getValue(); 
        }

        document.add(table);
        document.add(new Paragraph("\nTotal Movimentado: R$ " + String.format("%.2f", saldoTotal)));
        document.close();
    }
	
	public void exportarExcelAnual(Map<String, Double> dadosAgrupados, int ano, String caminhoArquivo) throws Exception {
        
		try (Workbook workbook = new XSSFWorkbook()) {
            
			Sheet sheet = workbook.createSheet("Relatório Anual " + ano);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Categoria");
            headerRow.createCell(1).setCellValue("Total (R$)");

            int rowNum = 1;
            double totalGeral = 0;

            for (Map.Entry<String, Double> entry : dadosAgrupados.entrySet()) {
                
            	Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
                
                totalGeral += entry.getValue();
            }

            Row totalRow = sheet.createRow(rowNum + 1);
            Cell cellTitulo = totalRow.createCell(0);
            cellTitulo.setCellValue("TOTAL MOVIMENTADO:");
            
            CellStyle styleBold = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            styleBold.setFont(font);
            cellTitulo.setCellStyle(styleBold);

            Cell cellValor = totalRow.createCell(1);
            cellValor.setCellValue(totalGeral);
            cellValor.setCellStyle(styleBold);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            try (FileOutputStream fileOut = new FileOutputStream(caminhoArquivo)) {
                workbook.write(fileOut);
            }
        }
    }
	
	private Map<Integer, String> carregarMapaCategorias(){
		
		Map<Integer, String> map = new HashMap<>();
		try {
			
			List<Categoria> lista = categoriaService.listarCategorias();
			for(Categoria c : lista) map.put(c.getId(), c.getNome());
			
		} catch(Exception ignored) {}
		return map;
	}
	
	private Map<Integer, String> carregarMapaContas(){
		
		Map<Integer, String> map = new HashMap<>();
		try {
			
			List<Conta> lista = contaService.listarContas();
			for(Conta c : lista) map.put(c.getId(), c.getNomeBanco());
			
		} catch(Exception ignored) {}
		return map;
	}
	
}
