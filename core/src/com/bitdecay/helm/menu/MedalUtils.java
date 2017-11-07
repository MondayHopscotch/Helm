package com.bitdecay.helm.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.unlock.StatName;

/**
 * Created by Monday on 4/9/2017.
 */

public class MedalUtils {
    private static TextureRegion bronzeMedalImg;
    private static TextureRegion silverMedalImg;
    private static TextureRegion goldMedalImg;
    private static TextureRegion devMedalImg;

    public static int imageSize;

    public static String UnrankedMedalName = "Unranked";
    public static String BronzeMedalName = "Bronze";
    public static String SilverMedalName = "Silver";
    public static String GoldMedalName = "Gold";
    public static String DeveloperMedalName = "Developer";

    public enum LevelRating {
        UNRANKED(StatName.NO_STAT, UnrankedMedalName),
        BRONZE(StatName.BRONZE_MEDALS, BronzeMedalName),
        SILVER(StatName.SILVER_MEDALS, SilverMedalName),
        GOLD(StatName.GOLD_MEDALS, GoldMedalName),
        DEV(StatName.DEV_MEDALS, DeveloperMedalName);


        private StatName statName;
        private String medalName;

        LevelRating(StatName statName, String medalName) {
            this.statName = statName;
            this.medalName = medalName;
        }

        public StatName statName() {
            return statName;
        }

        public String medalName() {
            return this.medalName;
        }

        public LevelRating nextRank() {
            int ordinal = this.ordinal();
            if (ordinal >= LevelRating.values().length) {
                // there is no next, return this one
                return this;
            }

            return LevelRating.values()[ordinal+1];
        }
    }

    public static void init(Helm game) {
        bronzeMedalImg = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("bronze_medal");
        silverMedalImg = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("silver_medal");
        goldMedalImg = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("gold_medal");
        devMedalImg = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("dev_medal");

        imageSize = (int) (game.fontScale * 16);
    }

    public static Image getIconForHighScore(com.bitdecay.helm.world.LevelInstance level) {
        LevelRating rank = getScoreRank(level, level.getHighScore());
        return getRankImage(rank);
    }

    public static Image getIconForScore(com.bitdecay.helm.world.LevelInstance level, int score) {
        LevelRating rank = getScoreRank(level, score);
        return getRankImage(rank);
    }

    public static Image getIconForBestTime(com.bitdecay.helm.world.LevelInstance level) {
        LevelRating rank = getTimeRank(level, level.getBestTime());
        return getRankImage(rank);
    }

    public static Image getIconForBestTime(com.bitdecay.helm.world.LevelInstance level, float time) {
        LevelRating rank = getTimeRank(level, time);
        return getRankImage(rank);
    }


    public static LevelRating getScoreRank(com.bitdecay.helm.world.LevelInstance level, int score) {
        if (score >= level.levelDef.devScore) {
            return LevelRating.DEV;
        } else if (score >= level.levelDef.goldScore) {
            return LevelRating.GOLD;
        } else if (score >= level.levelDef.silverScore) {
            return LevelRating.SILVER;
        } else if (score >= level.levelDef.bronzeScore) {
            return LevelRating.BRONZE;
        } else {
            return LevelRating.UNRANKED;
        }
    }

    public static LevelRating getTimeRank(com.bitdecay.helm.world.LevelInstance level, float time) {
        // we do this so that float precision doesn't mess with us.
        int userTime = (int) (time * 1000);
        int devTime = (int) (level.levelDef.devTime * 1000);
        int goldTime = (int) (level.levelDef.goldTime * 1000);
        int silverTime = (int) (level.levelDef.silverTime * 1000);
        int bronzeTime = (int) (level.levelDef.bronzeTime * 1000);
        if (userTime <= devTime) {
            return LevelRating.DEV;
        } else if (userTime <= goldTime) {
            return LevelRating.GOLD;
        } else if (userTime <= silverTime) {
            return LevelRating.SILVER;
        } else if (userTime <= bronzeTime) {
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
                return new Image(goldMedalImg);
            case DEV:
                return new Image(devMedalImg);
            default:
                return new Image();
        }
    }
}
