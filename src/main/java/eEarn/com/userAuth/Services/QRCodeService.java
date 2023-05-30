package eEarn.com.userAuth.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;


@Service
@Slf4j
public class QRCodeService {

    public byte[] createQRCode(String secret,int width,int height){
        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();;
        QRCodeWriter qrCodeWriter=new QRCodeWriter();
        try {
            BitMatrix encode = qrCodeWriter.encode(secret, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageConfig config=new MatrixToImageConfig(0xFF000000,0xFFFFFFFF);
            //MatrixToImageWriter.writeToStream(encode,"PNG",outputStream);
            MatrixToImageWriter.writeToStream(encode,"PNG",outputStream,config);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(outputStream.toByteArray().toString());
        return outputStream.toByteArray();
    }
    private static final String QR_CODE_IMAGE_PATH = "./MyQRCode.png";

    public  void generateQRCodeImage(String text, int width, int height, String filePath)
             {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
                 BitMatrix bitMatrix = null;
                 try {
                     bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
                     Path path = FileSystems.getDefault().getPath(filePath);
                     MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
                 } catch (WriterException | IOException e) {
                     throw new RuntimeException(e);
                 }


    }
}
