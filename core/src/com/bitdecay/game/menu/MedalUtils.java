package com.bitdecay.game.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bitdecay.game.Helm;
import com.bitdecay.game.world.LevelInstance;

/**
 * Created by Monday on 4/9/2017.
 */

public class MedalUtils {
    private static TextureRegion bronzeMedalImg;
    private static TextureRegion silverMedalImg;
    private static TextureRegion goldMedalImg;

    public static int imageSize;

    public enum LevelRating {
        UNRANKED,
        BRONZE,
        SILVER,
        GOLD,
        DEV
    }

    public static void init(Helm game) {
        bronzeMedalImg = game.assets.get("img/medals.atlas", TextureAtlas.class).findRegion("bronze_medal");
        silverMedalImg = game.assets.get("img/medals.atlas", TextureAtlas.class).findRegion("silver_medal");
        goldMedalImg = game.assets.get("img/medals.atlas", TextureAtlas.class).findRegion("gold_medal");

        imageSize = (int) (game.fontScale * 16);
    }

    public static Image getIconForHighScore(LevelInstance level) {
        LevelRating rank = getScoreRank(level, level.getHighScore());
        return getRankImage(rank);
    }

    public static Image getIconForScore(LevelInstance level, int score) {
        LevelRating rank = getScoreRank(level, score);
        return getRankImage(rank);
    }

    public static Image getIconForBestTime(LevelInstance level) {
        LevelRating rank = getTimeRank(level, level.getBestTime());
        return getRankImage(rank);
    }

    public static Image getIconForBestTime(LevelInstance level, float time) {
        LevelRating rank = getTimeRank(level, time);
        return getRankImage(rank);
    }


    public static LevelRating getScoreRank(LevelInstance level, int score) {
        if (score > level.levelDef.devScore) {
            return LevelRating.DEV;
        } else if (score > level.levelDef.goldScore) {
            return LevelRating.GOLD;
        } else if (score > level.levelDef.silverScore) {
            return LevelRating.SILVER;
        } else if (score > level.levelDef.bronzeScore) {
            return LevelRating.BRONZE;
        } else {
            return LevelRating.UNRANKED;
        }
    }

    public static LevelRating getTimeRank(LevelInstance level, float time) {
        if (time < level.levelDef.devTime) {
            return LevelRating.DEV;
        } else if (time < level.levelDef.goldTime) {
            return LevelRating.GOLD;
        } else if (time < level.levelDef.silverTime) {
            return LevelRating.SILVER;
        } else if (time < level.levelDef.bronzeTime) {
            return LevelRating.BRONZE;
        } else {
            return LevelRating.UNRANKED;
        }
    }

    public static Image getRankImage(LevelRating rank) {
        switch(rank) {
            case UNRANKED:
                return new Image();
            case BRONZE:
                return new Image(bronzeMedalImg);
            case SILVER:
                return new Image(silverMedalImg);
            case GOLD:
                new Image(goldMedalImg);
            case DEV:
                return new Image(goldMedalImg);
            default:
                return new Image();
        }
    }
}
