package com.bitdecay.game.desktop.packer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Monday on 4/8/2017.
 */

public class HelmPacker {

    private static final FileFilter directoryFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };

    public static void packAllTextures(String inDir, String outDir) {
        packAllTextures(new File(inDir), new File(outDir));
    }

    public static void packAllTextures(File inputDir, File outputDir) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.combineSubdirectories = true;
        settings.duplicatePadding = true;
        settings.fast = true;
        settings.filterMin = Texture.TextureFilter.Nearest;
        settings.filterMag = Texture.TextureFilter.Nearest;

        System.out.println("Starting to pack textures...");
        int count = 0;
        long startTime = System.currentTimeMillis();
        for (File subDir : inputDir.listFiles(directoryFilter)) {
            count++;
            System.out.println("\n****Packing atlas '" + subDir.getName() + "'");
            TexturePacker.process(settings, subDir.getAbsolutePath(), outputDir.getAbsolutePath(), subDir.getName());
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Finished packing textures");
        System.out.println("Atlases Packed: " + count);
        System.out.println(String.format("Total time: %.2f seconds", duration / 1000f));
    }
}
