package vn.mobileid.paperless.utils;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfReverse;
import com.itextpdf.kernel.pdf.ReaderProperties;

public class Itext7CommonFunction {

    public static byte[] RemoveSignaturesFromDocument(byte[] pdf) {
        // TODO implement here
        try {
            while (CommonFunction.checkSign(pdf)) {
                try {
                    System.out.println("RemoveSignaturesFromDocument" + CommonFunction.checkSign(pdf));
                    PdfReader reader = new PdfReader(new RandomAccessSourceFactory()
                            .setForceRead(false).createSource(pdf),
                            new ReaderProperties(),
                            true);
                    byte[] previous = PdfReverse.getPreviousVersionFilePdf(reader);
                    reader.close();
                    if (previous == null) {
                        System.out.println("Cannot continue to getPrevious File!!");
                        break;
                    }
                    pdf = previous;
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                    // Thực hiện xử lý cho ngoại lệ cụ thể nếu cần thiết
                    // Ví dụ: ghi log, báo cáo lỗi, ...
                    // Nếu không cần xử lý ngoại lệ cụ thể, hãy để nó lan truyền lên cấp trên
                    throw new RuntimeException("Error processing PDF", e);
                }
            }
        } catch (IOException | java.io.IOException ex) {
            System.out.println("Exception: " + ex);
            // Xử lý hoặc ghi log cho ngoại lệ IOException nếu cần thiết
            throw new RuntimeException("Error processing PDF", ex);
        }
        return pdf;
    }
}
