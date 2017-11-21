package com.orbitmines.spigot.api._2fa;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.madblock.spigot.api.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.awt.*;

/*
* MadBlock, LLC CONFIDENTIAL - @author Fadi Shawki - 2017
* __________________
*
*  2017 MadBlock, LLC 
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains
* the property of MadBlock, LLC and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to MadBlock, LLC
* and its suppliers and may be covered by U.S. and Foreign Patents,
* patents in process, and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from MadBlock, LLC.
*/
public class QRMapRenderer extends MapRenderer {

    private OMPlayer omp;
    private final String secret;
    private final BitMatrix bitMatrix;

    public QRMapRenderer(_2FA _2fa, OMPlayer omp, String secret) throws WriterException {
        this.mbp = mbp;
        this.secret = secret;
        this.bitMatrix = new QRCodeWriter().encode(_2fa.otpAuth(mbp, secret), BarcodeFormat.QR_CODE, 128, 128);
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
