package com.lincz.blog.util;

import com.lincz.blog.entity.Account;
import com.lincz.blog.service.AccountService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AccountUtils {



    BufferedImage createResizedCopy(Image originalImage,
                                    int scaledWidth, int scaledHeight,
                                    boolean preserveAlpha)
    {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }



    public static void resizeImage(File originalFile, File resizedFile, int newWidth) throws IOException {
        ImageIcon imageIcon = new ImageIcon(originalFile.getCanonicalPath());
        Image image = imageIcon.getImage();
        Image resizedImage = null;
        int iWidth = image.getWidth(null);
        int iHeight = image.getHeight(null);
        if (iWidth > iHeight) {
            resizedImage = image.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
        } else {
            resizedImage = image.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
        }
        Image temp = new ImageIcon(resizedImage).getImage();
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        graphics.drawImage(temp, 0, 0, null);
        graphics.dispose();
        float softenFactor = 0.05f;
        float[] softenArray = { 0, softenFactor, 0, softenFactor,
                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);
        FileOutputStream out = new FileOutputStream(resizedFile);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam jpegEncodeParam = encoder.getDefaultJPEGEncodeParam(bufferedImage);
        jpegEncodeParam.setQuality(1, true);
        encoder.setJPEGEncodeParam(jpegEncodeParam);
        encoder.encode(bufferedImage);
    }


}
