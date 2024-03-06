package edu.esprit.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import edu.esprit.entities.Exposition;
import edu.esprit.entities.Reservation;
import edu.esprit.entities.User;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfGenerator {
    public static void generatePDF(Exposition exposition, User user, Reservation reservation) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Reservation.pdf"));
            document.open();

            PdfContentByte canvas = writer.getDirectContentUnder();
            Image backgroundImage = Image.getInstance(PdfGenerator.class.getResource("/assets/back_pdf.jpg"));
            backgroundImage.setAbsolutePosition(0, 0);
            backgroundImage.scaleAbsolute(document.getPageSize());
            canvas.addImage(backgroundImage);


            Rectangle rect = new Rectangle(577, 825, 18, 15);
            rect.enableBorderSide(1);
            rect.enableBorderSide(2);
            rect.enableBorderSide(4);
            rect.enableBorderSide(8);
            rect.setBorderColor(BaseColor.WHITE);
            rect.setBorderWidth(5);
            document.add(rect);

            LineSeparator ls = new LineSeparator();
            ls.setLineColor(BaseColor.WHITE);
            document.add(new Chunk(ls));

            Chunk chunk = new Chunk("Confirmation de la Reservation");
            Paragraph p = new Paragraph(chunk);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            Chunk dateChunk = new Chunk(date);
            Paragraph dateParagraph = new Paragraph(dateChunk);
            dateParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(new Chunk(ls));
            document.add(dateParagraph);

            document.add(new Chunk(ls));

            Paragraph clientParagraph = new Paragraph();
            clientParagraph.add(new Phrase("Cher(e) " + user.getNom_user() + " " + user.getPrenom_user() + ",\n\n"));
            clientParagraph.add(new Phrase("Merci d'avoir réservé une exposition dans notre salle de gallerie. Nous sommes impatients de vous accueillir etc.\n\n"));
            clientParagraph.add(new Phrase("Ci-dessous, vous trouverez les détails de votre réservation :\n\n"));
            document.add(clientParagraph);

            document.add(new Paragraph("\n\n\n\n\n"));

            PdfPTable table = new PdfPTable(1);

            PdfPCell cell;
            p = new Paragraph("Nom de l'exposition : " + exposition.getNom());
            cell = new PdfPCell(p);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(10);
            cell.setBackgroundColor(new BaseColor(250, 240, 230)); // Couleur beige
            table.addCell(cell);

            p = new Paragraph("Nom et prénom du client : " + user.getNom_user() + " " + user.getPrenom_user() );
            cell = new PdfPCell(p);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(10);
            cell.setBackgroundColor(new BaseColor(250, 240, 230)); // Couleur beige
            table.addCell(cell);

            p = new Paragraph("nombre de ticket " + reservation.getTicketsNumber());
            cell = new PdfPCell(p);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(10);
            cell.setBackgroundColor(new BaseColor(250, 240, 230)); // Couleur beige
            table.addCell(cell);

            p = new Paragraph("Date debut : " + exposition.getDateDebut());
            cell = new PdfPCell(p);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(10);
            cell.setBackgroundColor(new BaseColor(250, 240, 230)); // Couleur beige
            table.addCell(cell);
            p = new Paragraph("Date fin : " + exposition.getDateDebut());
            cell = new PdfPCell(p);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(10);
            cell.setBackgroundColor(new BaseColor(250, 240, 230)); // Couleur beige
            table.addCell(cell);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String heureDebutFormatted = dateFormat.format(exposition.getHeure_debut());
            String heureFinFormatted = dateFormat.format(exposition.getHeure_fin());

            p = new Paragraph("Heures : " + heureDebutFormatted + " jusqu'à " + heureFinFormatted);
            cell = new PdfPCell(p);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(10);
            cell.setBackgroundColor(new BaseColor(250, 240, 230)); // Couleur beige
            table.addCell(cell);


            document.add(table);

            document.add(new Paragraph("\n\n\n\n\n"));

            Paragraph footer = new Paragraph("MuseMakers Gallery, 1, 2 rue André Ampère - 2083 - Pôle Technologique - El Ghazala.\nTel : 71500500,\nEmail : musemakers@gmail.com");
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            Image logo = Image.getInstance(PdfGenerator.class.getResource("/assets/logo-bg.png"));

            float desiredWidth = 100;
            float desiredHeight = 100;
            logo.scaleAbsolute(desiredWidth, desiredHeight);

            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);


            document.close();
            File pdfFile = new File("Reservation.pdf");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}