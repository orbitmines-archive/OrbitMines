package com.orbitmines.spigot.api._2fa;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.awt.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class QRMapRenderer extends MapRenderer {

    private OMPlayer omp;
    private final String secret;
    private final BitMatrix bitMatrix;

    public QRMapRenderer(_2FA _2fa, OMPlayer omp, String secret) throws WriterException {
        this.omp = omp;
        this.secret = secret;
        this.bitMatrix = new QRCodeWriter().encode(_2fa.otpAuth(omp, secret), BarcodeFormat.QR_CODE, 128, 128);
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        for (int x = 0; x < 128; x++) {
            for (int z = 0; z < 128; z++) {
                if (z >= 14)
                    mapCanvas.setPixel(x, z, bitMatrix.get(x, z - 14) ? MapPalette.matchColor(Color.BLACK) : MapPalette.WHITE);
                else
                    mapCanvas.setPixel(x, z, MapPalette.WHITE);
            }
        }
        mapCanvas.drawText(5, 5, MinecraftFont.Font, "OrbitMines 2FA");
        mapCanvas.drawText(15, 15, MinecraftFont.Font, "Player: " + player.getName());
        mapCanvas.drawText(15, 25, MinecraftFont.Font, secret);
    }
}
