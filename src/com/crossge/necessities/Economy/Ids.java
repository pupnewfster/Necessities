package com.crossge.necessities.Economy;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public class Ids {
	private File configFileIds = new File("plugins/Necessities/Economy", "ids.yml");
	
	public void setIds() {
		YamlConfiguration configIds = YamlConfiguration.loadConfiguration(configFileIds);
		//TODO: add 1.8 items and ids
		if(!configIds.contains("AIR"))
			configIds.set("AIR", 0);
		if(!configIds.contains("STONE"))
			configIds.set("STONE", 1);
		if(!configIds.contains("GRASS"))
			configIds.set("GRASS", 2);
		if(!configIds.contains("DIRT"))
			configIds.set("DIRT", 3);
		if(!configIds.contains("COBBLESTONE"))
			configIds.set("COBBLESTONE", 4);
		if(!configIds.contains("WOOD"))
			configIds.set("WOOD", 5);
		if(!configIds.contains("SAPLING"))
			configIds.set("SAPLING", 6);
		if(!configIds.contains("BEDROCK"))
			configIds.set("BEDROCK", 7);
		if(!configIds.contains("WATER"))
			configIds.set("WATER", 8);
		if(!configIds.contains("STATIONARY_WATER"))
			configIds.set("STATIONARY_WATER", 9);
		if(!configIds.contains("LAVA"))
			configIds.set("LAVA", 10);
		if(!configIds.contains("STATIONARY_LAVA"))
			configIds.set("STATIONARY_LAVA", 11);
		if(!configIds.contains("SAND"))
			configIds.set("SAND", 12);
		if(!configIds.contains("GRAVEL"))
			configIds.set("GRAVEL", 13);
		if(!configIds.contains("GOLD_ORE"))
			configIds.set("GOLD_ORE", 14);
		if(!configIds.contains("IRON_ORE"))
			configIds.set("IRON_ORE", 15);
		if(!configIds.contains("COAL_ORE"))
			configIds.set("COAL_ORE", 16);
		if(!configIds.contains("LOG"))
			configIds.set("LOG", 17);
		if(!configIds.contains("LEAVES"))
			configIds.set("LEAVES", 18);
		if(!configIds.contains("SPONGE"))
			configIds.set("SPONGE", 19);
		if(!configIds.contains("GLASS"))
			configIds.set("GLASS", 20);
		if(!configIds.contains("LAPIS_ORE"))
			configIds.set("LAPIS_ORE", 21);
		if(!configIds.contains("LAPIS_BLOCK"))
			configIds.set("LAPIS_BLOCK", 22);
		if(!configIds.contains("DISPENSER"))
			configIds.set("DISPENSER", 23);
		if(!configIds.contains("SANDSTONE"))
			configIds.set("SANDSTONE", 24);
		if(!configIds.contains("NOTE_BLOCK"))
			configIds.set("NOTE_BLOCK", 25);
		if(!configIds.contains("BED_BLOCK"))
			configIds.set("BED_BLOCK", 26);
		if(!configIds.contains("POWERED_RAIL"))
			configIds.set("POWERED_RAIL", 27);
		if(!configIds.contains("DETECTOR_RAIL"))
			configIds.set("DETECTOR_RAIL", 28);
		if(!configIds.contains("PISTON_STICKY_BASE"))
			configIds.set("PISTON_STICKY_BASE", 29);
		if(!configIds.contains("WEB"))
			configIds.set("WEB", 30);
		if(!configIds.contains("LONG_GRASS"))
			configIds.set("LONG_GRASS", 31);
		if(!configIds.contains("DEAD_BUSH"))
			configIds.set("DEAD_BUSH", 32);
		if(!configIds.contains("PISTON_BASE"))
			configIds.set("PISTON_BASE", 33);
		if(!configIds.contains("PISTON_EXTENSION"))
			configIds.set("PISTON_EXTENSION", 34);
		if(!configIds.contains("WOOL"))
			configIds.set("WOOL", 35);
		if(!configIds.contains("PISTON_MOVING_PIECE"))
			configIds.set("PISTON_MOVING_PIECE", 36);
		if(!configIds.contains("YELLOW_FLOWER"))
			configIds.set("YELLOW_FLOWER", 37);
		if(!configIds.contains("RED_ROSE"))
			configIds.set("RED_ROSE", 38);
		if(!configIds.contains("BROWN_MUSHROOM"))
			configIds.set("BROWN_MUSHROOM", 39);
		if(!configIds.contains("RED_MUSHROOM"))
			configIds.set("RED_MUSHROOM", 40);
		if(!configIds.contains("GOLD_BLOCK"))
			configIds.set("GOLD_BLOCK", 41);
		if(!configIds.contains("IRON_BLOCK"))
			configIds.set("IRON_BLOCK", 42);
		if(!configIds.contains("DOUBLE_STEP"))
			configIds.set("DOUBLE_STEP", 43);
		if(!configIds.contains("STEP"))
			configIds.set("STEP", 44);
		if(!configIds.contains("BRICK"))
			configIds.set("BRICK", 45);
		if(!configIds.contains("TNT"))
			configIds.set("TNT", 46);
		if(!configIds.contains("BOOKSHELF"))
			configIds.set("BOOKSHELF", 47);
		if(!configIds.contains("MOSSY_COBBLESTONE"))
			configIds.set("MOSSY_COBBLESTONE", 48);
		if(!configIds.contains("OBSIDIAN"))
			configIds.set("OBSIDIAN", 49);
		if(!configIds.contains("TORCH"))
			configIds.set("TORCH", 50);
		if(!configIds.contains("FIRE"))
			configIds.set("FIRE", 51);
		if(!configIds.contains("MOB_SPAWNER"))
			configIds.set("MOB_SPAWNER", 52);
		if(!configIds.contains("WOOD_STAIRS"))
			configIds.set("WOOD_STAIRS", 53);
		if(!configIds.contains("CHEST"))
			configIds.set("CHEST", 54);
		if(!configIds.contains("REDSTONE_WIRE"))
			configIds.set("REDSTONE_WIRE", 55);
		if(!configIds.contains("DIAMOND_ORE"))
			configIds.set("DIAMOND_ORE", 56);
		if(!configIds.contains("DIAMOND_BLOCK"))
			configIds.set("DIAMOND_BLOCK", 57);
		if(!configIds.contains("WORKBENCH"))
			configIds.set("WORKBENCH", 58);
		if(!configIds.contains("CROPS"))
			configIds.set("CROPS", 59);
		if(!configIds.contains("SOIL"))
			configIds.set("SOIL", 60);
		if(!configIds.contains("FURNACE"))
			configIds.set("FURNACE", 61);
		if(!configIds.contains("BURNING_FURNACE"))
			configIds.set("BURNING_FURNACE", 62);
		if(!configIds.contains("SIGN_POST"))
			configIds.set("SIGN_POST", 63);
		if(!configIds.contains("WOODEN_DOOR"))
			configIds.set("WOODEN_DOOR", 64);
		if(!configIds.contains("LADDER"))
			configIds.set("LADDER", 65);
		if(!configIds.contains("RAILS"))
			configIds.set("RAILS", 66);
		if(!configIds.contains("COBBLESTONE_STAIRS"))
			configIds.set("COBBLESTONE_STAIRS", 67);
		if(!configIds.contains("WALL_SIGN"))
			configIds.set("WALL_SIGN", 68);
		if(!configIds.contains("LEVER"))
			configIds.set("LEVER", 69);
		if(!configIds.contains("STONE_PLATE"))
			configIds.set("STONE_PLATE", 70);
		if(!configIds.contains("IRON_DOOR_BLOCK"))
			configIds.set("IRON_DOOR_BLOCK", 71);
		if(!configIds.contains("WOOD_PLATE"))
			configIds.set("WOOD_PLATE", 72);
		if(!configIds.contains("REDSTONE_ORE"))
			configIds.set("REDSTONE_ORE", 73);
		if(!configIds.contains("GLOWING_REDSTONE_ORE"))
			configIds.set("GLOWING_REDSTONE_ORE", 74);
		if(!configIds.contains("REDSTONE_TORCH_OFF"))
			configIds.set("REDSTONE_TORCH_OFF", 75);
		if(!configIds.contains("REDSTONE_TORCH_ON"))
			configIds.set("REDSTONE_TORCH_ON", 76);
		if(!configIds.contains("STONE_BUTTON"))
			configIds.set("STONE_BUTTON", 77);
		if(!configIds.contains("SNOW"))
			configIds.set("SNOW", 78);
		if(!configIds.contains("ICE"))
			configIds.set("ICE", 79);
		if(!configIds.contains("SNOW_BLOCK"))
			configIds.set("SNOW_BLOCK", 80);
		if(!configIds.contains("CACTUS"))
			configIds.set("CACTUS", 81);
		if(!configIds.contains("CLAY"))
			configIds.set("CLAY", 82);
		if(!configIds.contains("SUGAR_CANE_BLOCK"))
			configIds.set("SUGAR_CANE_BLOCK", 83);
		if(!configIds.contains("JUKEBOX"))
			configIds.set("JUKEBOX", 84);
		if(!configIds.contains("FENCE"))
			configIds.set("FENCE", 85);
		if(!configIds.contains("PUMPKIN"))
			configIds.set("PUMPKIN", 86);
		if(!configIds.contains("NETHERRACK"))
			configIds.set("NETHERRACK", 87);
		if(!configIds.contains("SOUL_SAND"))
			configIds.set("SOUL_SAND", 88);
		if(!configIds.contains("GLOWSTONE"))
			configIds.set("GLOWSTONE", 89);
		if(!configIds.contains("PORTAL"))
			configIds.set("PORTAL", 90);
		if(!configIds.contains("JACK_O_LANTERN"))
			configIds.set("JACK_O_LANTERN", 91);
		if(!configIds.contains("CAKE_BLOCK"))
			configIds.set("CAKE_BLOCK", 92);
		if(!configIds.contains("DIODE_BLOCK_OFF"))
			configIds.set("DIODE_BLOCK_OFF", 93);
		if(!configIds.contains("DIODE_BLOCK_ON"))
			configIds.set("DIODE_BLOCK_ON", 94);
		if(!configIds.contains("LOCKED_CHEST"))
			configIds.set("LOCKED_CHEST", 95);
		if(!configIds.contains("STAINED_GLASS"))
			configIds.set("STAINED_GLASS", 95);
		if(!configIds.contains("TRAP_DOOR"))
			configIds.set("TRAP_DOOR", 96);
		if(!configIds.contains("MONSTER_EGGS"))
			configIds.set("MONSTER_EGGS", 97);
		if(!configIds.contains("SMOOTH_BRICK"))
			configIds.set("SMOOTH_BRICK", 98);
		if(!configIds.contains("HUGE_MUSHROOM_1"))
			configIds.set("HUGE_MUSHROOM_1", 99);
		if(!configIds.contains("HUGE_MUSHROOM_2"))
			configIds.set("HUGE_MUSHROOM_2", 100);
		if(!configIds.contains("IRON_FENCE"))
			configIds.set("IRON_FENCE", 101);
		if(!configIds.contains("THIN_GLASS"))
			configIds.set("THIN_GLASS", 102);
		if(!configIds.contains("MELON_BLOCK"))
			configIds.set("MELON_BLOCK", 103);
		if(!configIds.contains("PUMPKIN_STEM"))
			configIds.set("PUMPKIN_STEM", 104);
		if(!configIds.contains("MELON_STEM"))
			configIds.set("MELON_STEM", 105);
		if(!configIds.contains("VINE"))
			configIds.set("VINE", 106);
		if(!configIds.contains("FENCE_GATE"))
			configIds.set("FENCE_GATE", 107);
		if(!configIds.contains("BRICK_STAIRS"))
			configIds.set("BRICK_STAIRS", 108);
		if(!configIds.contains("SMOOTH_STAIRS"))
			configIds.set("SMOOTH_STAIRS", 109);
		if(!configIds.contains("MYCEL"))
			configIds.set("MYCEL", 110);
		if(!configIds.contains("WATER_LILY"))
			configIds.set("WATER_LILY", 111);
		if(!configIds.contains("NETHER_BRICK"))
			configIds.set("NETHER_BRICK", 112);
		if(!configIds.contains("NETHER_FENCE"))
			configIds.set("NETHER_FENCE", 113);
		if(!configIds.contains("NETHER_BRICK_STAIRS"))
			configIds.set("NETHER_BRICK_STAIRS", 114);
		if(!configIds.contains("NETHER_WARTS"))
			configIds.set("NETHER_WARTS", 115);
		if(!configIds.contains("ENCHANTMENT_TABLE"))
			configIds.set("ENCHANTMENT_TABLE", 116);
		if(!configIds.contains("BREWING_STAND"))
			configIds.set("BREWING_STAND", 117);
		if(!configIds.contains("CAULDRON"))
			configIds.set("CAULDRON", 118);
		if(!configIds.contains("ENDER_PORTAL"))
			configIds.set("ENDER_PORTAL", 119);
		if(!configIds.contains("ENDER_PORTAL_FRAME"))
			configIds.set("ENDER_PORTAL_FRAME", 120);
		if(!configIds.contains("ENDER_STONE"))
			configIds.set("ENDER_STONE", 121);
		if(!configIds.contains("DRAGON_EGG"))
			configIds.set("DRAGON_EGG", 122);
		if(!configIds.contains("REDSTONE_LAMP_OFF"))
			configIds.set("REDSTONE_LAMP_OFF", 123);
		if(!configIds.contains("REDSTONE_LAMP_ON"))
			configIds.set("REDSTONE_LAMP_ON", 124);
		if(!configIds.contains("WOOD_DOUBLE_STEP"))
			configIds.set("WOOD_DOUBLE_STEP", 125);
		if(!configIds.contains("WOOD_STEP"))
			configIds.set("WOOD_STEP", 126);
		if(!configIds.contains("COCOA"))
			configIds.set("COCOA", 127);
		if(!configIds.contains("SANDSTONE_STAIRS"))
			configIds.set("SANDSTONE_STAIRS", 128);
		if(!configIds.contains("EMERALD_ORE"))
			configIds.set("EMERALD_ORE", 129);
		if(!configIds.contains("ENDER_CHEST"))
			configIds.set("ENDER_CHEST", 130);
		if(!configIds.contains("TRIPWIRE_HOOK"))
			configIds.set("TRIPWIRE_HOOK", 131);
		if(!configIds.contains("TRIPWIRE"))
			configIds.set("TRIPWIRE", 132);
		if(!configIds.contains("EMERALD_BLOCK"))
			configIds.set("EMERALD_BLOCK", 133);
		if(!configIds.contains("SPRUCE_WOOD_STAIRS"))
			configIds.set("SPRUCE_WOOD_STAIRS", 134);
		if(!configIds.contains("BIRCH_WOOD_STAIRS"))
			configIds.set("BIRCH_WOOD_STAIRS", 135);
		if(!configIds.contains("JUNGLE_WOOD_STAIRS"))
			configIds.set("JUNGLE_WOOD_STAIRS", 136);
		if(!configIds.contains("COMMAND"))
			configIds.set("COMMAND", 137);
		if(!configIds.contains("BEACON"))
			configIds.set("BEACON", 138);
		if(!configIds.contains("COBBLE_WALL"))
			configIds.set("COBBLE_WALL", 139);
		if(!configIds.contains("FLOWER_POT"))
			configIds.set("FLOWER_POT", 140);
		if(!configIds.contains("CARROT"))
			configIds.set("CARROT", 141);
		if(!configIds.contains("POTATO"))
			configIds.set("POTATO", 142);
		if(!configIds.contains("WOOD_BUTTON"))
			configIds.set("WOOD_BUTTON", 143);
		if(!configIds.contains("SKULL"))
			configIds.set("SKULL", 144);
		if(!configIds.contains("ANVIL"))
			configIds.set("ANVIL", 145);
		if(!configIds.contains("TRAPPED_CHEST"))
			configIds.set("TRAPPED_CHEST", 146);
		if(!configIds.contains("GOLD_PLATE"))
			configIds.set("GOLD_PLATE", 147);
		if(!configIds.contains("IRON_PLATE"))
			configIds.set("IRON_PLATE", 148);
		if(!configIds.contains("REDSTONE_COMPARATOR_OFF"))
			configIds.set("REDSTONE_COMPARATOR_OFF", 149);
		if(!configIds.contains("REDSTONE_COMPARATOR_ON"))
			configIds.set("REDSTONE_COMPARATOR_ON", 150);
		if(!configIds.contains("DAYLIGHT_DETECTOR"))
			configIds.set("DAYLIGHT_DETECTOR", 151);
		if(!configIds.contains("REDSTONE_BLOCK"))
			configIds.set("REDSTONE_BLOCK", 152);
		if(!configIds.contains("QUARTZ_ORE"))
			configIds.set("QUARTZ_ORE", 153);
		if(!configIds.contains("HOPPER"))
			configIds.set("HOPPER", 154);
		if(!configIds.contains("QUARTZ_BLOCK"))
			configIds.set("QUARTZ_BLOCK", 155);
		if(!configIds.contains("QUARTZ_STAIRS"))
			configIds.set("QUARTZ_STAIRS", 156);
		if(!configIds.contains("ACTIVATOR_RAIL"))
			configIds.set("ACTIVATOR_RAIL", 157);
		if(!configIds.contains("DROPPER"))
			configIds.set("DROPPER", 158);
		if(!configIds.contains("STAINED_CLAY"))
			configIds.set("STAINED_CLAY", 159);
		if(!configIds.contains("STAINED_GLASS_PANE"))
			configIds.set("STAINED_GLASS_PANE", 160);
		if(!configIds.contains("LEAVES_2"))
			configIds.set("LEAVES_2", 161);
		if(!configIds.contains("LOG_2"))
			configIds.set("LOG_2", 162);
		if(!configIds.contains("ACACIA_STAIRS"))
			configIds.set("ACACIA_STAIRS", 163);
		if(!configIds.contains("DARK_OAK_STAIRS"))
			configIds.set("DARK_OAK_STAIRS", 164);
		if(!configIds.contains("HAY_BLOCK"))
			configIds.set("HAY_BLOCK", 170);
		if(!configIds.contains("CARPET"))
			configIds.set("CARPET", 171);
		if(!configIds.contains("HARD_CLAY"))
			configIds.set("HARD_CLAY", 172);
		if(!configIds.contains("COAL_BLOCK"))
			configIds.set("COAL_BLOCK", 173);
		if(!configIds.contains("PACKED_ICE"))
			configIds.set("PACKED_ICE", 174);
		if(!configIds.contains("DOUBLE_PLANT"))
			configIds.set("DOUBLE_PLANT", 175);
		if(!configIds.contains("IRON_SPADE"))
			configIds.set("IRON_SPADE", 256);
		if(!configIds.contains("IRON_PICKAXE"))
			configIds.set("IRON_PICKAXE", 257);
		if(!configIds.contains("IRON_AXE"))
			configIds.set("IRON_AXE", 258);
		if(!configIds.contains("FLINT_AND_STEEL"))
			configIds.set("FLINT_AND_STEEL", 259);
		if(!configIds.contains("APPLE"))
			configIds.set("APPLE", 260);
		if(!configIds.contains("BOW"))
			configIds.set("BOW", 261);
		if(!configIds.contains("ARROW"))
			configIds.set("ARROW", 262);
		if(!configIds.contains("COAL"))
			configIds.set("COAL", 263);
		if(!configIds.contains("DIAMOND"))
			configIds.set("DIAMOND", 264);
		if(!configIds.contains("IRON_INGOT"))
			configIds.set("IRON_INGOT", 265);
		if(!configIds.contains("GOLD_INGOT"))
			configIds.set("GOLD_INGOT", 266);
		if(!configIds.contains("IRON_SWORD"))
			configIds.set("IRON_SWORD", 267);
		if(!configIds.contains("WOOD_SWORD"))
			configIds.set("WOOD_SWORD", 268);
		if(!configIds.contains("WOOD_SPADE"))
			configIds.set("WOOD_SPADE", 269);
		if(!configIds.contains("WOOD_PICKAXE"))
			configIds.set("WOOD_PICKAXE", 270);
		if(!configIds.contains("WOOD_AXE"))
			configIds.set("WOOD_AXE", 271);
		if(!configIds.contains("STONE_SWORD"))
			configIds.set("STONE_SWORD", 272);
		if(!configIds.contains("STONE_SPADE"))
			configIds.set("STONE_SPADE", 273);
		if(!configIds.contains("STONE_PICKAXE"))
			configIds.set("STONE_PICKAXE", 274);
		if(!configIds.contains("STONE_AXE"))
			configIds.set("STONE_AXE", 275);
		if(!configIds.contains("DIAMOND_SWORD"))
			configIds.set("DIAMOND_SWORD", 276);
		if(!configIds.contains("DIAMOND_SPADE"))
			configIds.set("DIAMOND_SPADE", 277);
		if(!configIds.contains("DIAMOND_PICKAXE"))
			configIds.set("DIAMOND_PICKAXE", 278);
		if(!configIds.contains("DIAMOND_AXE"))
			configIds.set("DIAMOND_AXE", 279);
		if(!configIds.contains("STICK"))
			configIds.set("STICK", 280);
		if(!configIds.contains("BOWL"))
			configIds.set("BOWL", 281);
		if(!configIds.contains("MUSHROOM_SOUP"))
			configIds.set("MUSHROOM_SOUP", 282);
		if(!configIds.contains("GOLD_SWORD"))
			configIds.set("GOLD_SWORD", 283);
		if(!configIds.contains("GOLD_SPADE"))
			configIds.set("GOLD_SPADE", 284);
		if(!configIds.contains("GOLD_PICKAXE"))
			configIds.set("GOLD_PICKAXE", 285);
		if(!configIds.contains("GOLD_AXE"))
			configIds.set("GOLD_AXE", 286);
		if(!configIds.contains("STRING"))
			configIds.set("STRING", 287);
		if(!configIds.contains("FEATHER"))
			configIds.set("FEATHER", 288);
		if(!configIds.contains("SULPHUR"))
			configIds.set("SULPHUR", 289);
		if(!configIds.contains("WOOD_HOE"))
			configIds.set("WOOD_HOE", 290);
		if(!configIds.contains("STONE_HOE"))
			configIds.set("STONE_HOE", 291);
		if(!configIds.contains("IRON_HOE"))
			configIds.set("IRON_HOE", 292);
		if(!configIds.contains("DIAMOND_HOE"))
			configIds.set("DIAMOND_HOE", 293);
		if(!configIds.contains("GOLD_HOE"))
			configIds.set("GOLD_HOE", 294);
		if(!configIds.contains("SEEDS"))
			configIds.set("SEEDS", 295);
		if(!configIds.contains("WHEAT"))
			configIds.set("WHEAT", 296);
		if(!configIds.contains("BREAD"))
			configIds.set("BREAD", 297);
		if(!configIds.contains("LEATHER_HELMET"))
			configIds.set("LEATHER_HELMET", 298);
		if(!configIds.contains("LEATHER_CHESTPLATE"))
			configIds.set("LEATHER_CHESTPLATE", 299);
		if(!configIds.contains("LEATHER_LEGGINGS"))
			configIds.set("LEATHER_LEGGINGS", 300);
		if(!configIds.contains("LEATHER_BOOTS"))
			configIds.set("LEATHER_BOOTS", 301);
		if(!configIds.contains("CHAINMAIL_HELMET"))
			configIds.set("CHAINMAIL_HELMET", 302);
		if(!configIds.contains("CHAINMAIL_CHESTPLATE"))
			configIds.set("CHAINMAIL_CHESTPLATE", 303);
		if(!configIds.contains("CHAINMAIL_LEGGINGS"))
			configIds.set("CHAINMAIL_LEGGINGS", 304);
		if(!configIds.contains("CHAINMAIL_BOOTS"))
			configIds.set("CHAINMAIL_BOOTS", 305);
		if(!configIds.contains("IRON_HELMET"))
			configIds.set("IRON_HELMET", 306);
		if(!configIds.contains("IRON_CHESTPLATE"))
			configIds.set("IRON_CHESTPLATE", 307);
		if(!configIds.contains("IRON_LEGGINGS"))
			configIds.set("IRON_LEGGINGS", 308);
		if(!configIds.contains("IRON_BOOTS"))
			configIds.set("IRON_BOOTS", 309);
		if(!configIds.contains("DIAMOND_HELMET"))
			configIds.set("DIAMOND_HELMET", 310);
		if(!configIds.contains("DIAMOND_CHESTPLATE"))
			configIds.set("DIAMOND_CHESTPLATE", 311);
		if(!configIds.contains("DIAMOND_LEGGINGS"))
			configIds.set("DIAMOND_LEGGINGS", 312);
		if(!configIds.contains("DIAMOND_BOOTS"))
			configIds.set("DIAMOND_BOOTS", 313);
		if(!configIds.contains("GOLD_HELMET"))
			configIds.set("GOLD_HELMET", 314);
		if(!configIds.contains("GOLD_CHESTPLATE"))
			configIds.set("GOLD_CHESTPLATE", 315);
		if(!configIds.contains("GOLD_LEGGINGS"))
			configIds.set("GOLD_LEGGINGS", 316);
		if(!configIds.contains("GOLD_BOOTS"))
			configIds.set("GOLD_BOOTS", 317);
		if(!configIds.contains("FLINT"))
			configIds.set("FLINT", 318);
		if(!configIds.contains("PORK"))
			configIds.set("PORK", 319);
		if(!configIds.contains("GRILLED_PORK"))
			configIds.set("GRILLED_PORK", 320);
		if(!configIds.contains("PAINTING"))
			configIds.set("PAINTING", 321);
		if(!configIds.contains("GOLDEN_APPLE"))
			configIds.set("GOLDEN_APPLE", 322);
		if(!configIds.contains("SIGN"))
			configIds.set("SIGN", 323);
		if(!configIds.contains("WOOD_DOOR"))
			configIds.set("WOOD_DOOR", 324);
		if(!configIds.contains("BUCKET"))
			configIds.set("BUCKET", 325);
		if(!configIds.contains("WATER_BUCKET"))
			configIds.set("WATER_BUCKET", 326);
		if(!configIds.contains("LAVA_BUCKET"))
			configIds.set("LAVA_BUCKET", 327);
		if(!configIds.contains("MINECART"))
			configIds.set("MINECART", 328);
		if(!configIds.contains("SADDLE"))
			configIds.set("SADDLE", 329);
		if(!configIds.contains("IRON_DOOR"))
			configIds.set("IRON_DOOR", 330);
		if(!configIds.contains("REDSTONE"))
			configIds.set("REDSTONE", 331);
		if(!configIds.contains("contains"))
			configIds.set("SNOW_BALL", 332);
		if(!configIds.contains("BOAT"))
			configIds.set("BOAT", 333);
		if(!configIds.contains("LEATHER"))
			configIds.set("LEATHER", 334);
		if(!configIds.contains("MILK_BUCKET"))
			configIds.set("MILK_BUCKET", 335);
		if(!configIds.contains("CLAY_BRICK"))
			configIds.set("CLAY_BRICK", 336);
		if(!configIds.contains("CLAY_BALL"))
			configIds.set("CLAY_BALL", 337);
		if(!configIds.contains("SUGAR_CANE"))
			configIds.set("SUGAR_CANE", 338);
		if(!configIds.contains("PAPER"))
			configIds.set("PAPER", 339);
		if(!configIds.contains("BOOK"))
			configIds.set("BOOK", 340);
		if(!configIds.contains("SLIME_BALL"))
			configIds.set("SLIME_BALL", 341);
		if(!configIds.contains("STORAGE_MINECART"))
			configIds.set("STORAGE_MINECART", 342);
		if(!configIds.contains("POWERED_MINECART"))
			configIds.set("POWERED_MINECART", 343);
		if(!configIds.contains("EGG"))
			configIds.set("EGG", 344);
		if(!configIds.contains("COMPASS"))
			configIds.set("COMPASS", 345);
		if(!configIds.contains("FISHING_ROD"))
			configIds.set("FISHING_ROD", 346);
		if(!configIds.contains("WATCH"))
			configIds.set("WATCH", 347);
		if(!configIds.contains("GLOWSTONE_DUST"))
			configIds.set("GLOWSTONE_DUST", 348);
		if(!configIds.contains("RAW_FISH"))
			configIds.set("RAW_FISH", 349);
		if(!configIds.contains("COOKED_FISH"))
			configIds.set("COOKED_FISH", 350);
		if(!configIds.contains("INK_SACK"))
			configIds.set("INK_SACK", 351);
		if(!configIds.contains("BONE"))
			configIds.set("BONE", 352);
		if(!configIds.contains("SUGAR"))
			configIds.set("SUGAR", 353);
		if(!configIds.contains("CAKE"))
			configIds.set("CAKE", 354);
		if(!configIds.contains("BED"))
			configIds.set("BED", 355);
		if(!configIds.contains("DIODE"))
			configIds.set("DIODE", 356);
		if(!configIds.contains("COOKIE"))
			configIds.set("COOKIE", 357);
		if(!configIds.contains("MAP"))
			configIds.set("MAP", 358);
		if(!configIds.contains("SHEARS"))
			configIds.set("SHEARS", 359);
		if(!configIds.contains("MELON"))
			configIds.set("MELON", 360);
		if(!configIds.contains("PUMPKIN_SEEDS"))
			configIds.set("PUMPKIN_SEEDS", 361);
		if(!configIds.contains("MELON_SEEDS"))
			configIds.set("MELON_SEEDS", 362);
		if(!configIds.contains("RAW_BEEF"))
			configIds.set("RAW_BEEF", 363);
		if(!configIds.contains("COOKED_BEEF"))
			configIds.set("COOKED_BEEF", 364);
		if(!configIds.contains("RAW_CHICKEN"))
			configIds.set("RAW_CHICKEN", 365);
		if(!configIds.contains("COOKED_CHICKEN"))
			configIds.set("COOKED_CHICKEN", 366);
		if(!configIds.contains("ROTTEN_FLESH"))
			configIds.set("ROTTEN_FLESH", 367);
		if(!configIds.contains("ENDER_PEARL"))
			configIds.set("ENDER_PEARL", 368);
		if(!configIds.contains("BLAZE_ROD"))
			configIds.set("BLAZE_ROD", 369);
		if(!configIds.contains("GHAST_TEAR"))
			configIds.set("GHAST_TEAR", 370);
		if(!configIds.contains("GOLD_NUGGET"))
			configIds.set("GOLD_NUGGET", 371);
		if(!configIds.contains("NETHER_STALK"))
			configIds.set("NETHER_STALK", 372);
		if(!configIds.contains("POTION"))
			configIds.set("POTION", 373);
		if(!configIds.contains("GLASS_BOTTLE"))
			configIds.set("GLASS_BOTTLE", 374);
		if(!configIds.contains("SPIDER_EYE"))
			configIds.set("SPIDER_EYE", 375);
		if(!configIds.contains("FERMENTED_SPIDER_EYE"))
			configIds.set("FERMENTED_SPIDER_EYE", 376);
		if(!configIds.contains("BLAZE_POWDER"))
			configIds.set("BLAZE_POWDER", 377);
		if(!configIds.contains("MAGMA_CREAM"))
			configIds.set("MAGMA_CREAM", 378);
		if(!configIds.contains("BREWING_STAND_ITEM"))
			configIds.set("BREWING_STAND_ITEM", 379);
		if(!configIds.contains("CAULDRON_ITEM"))
			configIds.set("CAULDRON_ITEM", 380);
		if(!configIds.contains("EYE_OF_ENDER"))
			configIds.set("EYE_OF_ENDER", 381);
		if(!configIds.contains("SPECKLED_MELON"))
			configIds.set("SPECKLED_MELON", 382);
		if(!configIds.contains("MONSTER_EGG"))
			configIds.set("MONSTER_EGG", 383);
		if(!configIds.contains("EXP_BOTTLE"))
			configIds.set("EXP_BOTTLE", 384);
		if(!configIds.contains("FIREBALL"))
			configIds.set("FIREBALL", 385);
		if(!configIds.contains("BOOK_AND_QUILL"))
			configIds.set("BOOK_AND_QUILL", 386);
		if(!configIds.contains("WRITTEN_BOOK"))
			configIds.set("WRITTEN_BOOK", 387);
		if(!configIds.contains("EMERALD"))
			configIds.set("EMERALD", 388);
		if(!configIds.contains("ITEM_FRAME"))
			configIds.set("ITEM_FRAME", 389);
		if(!configIds.contains("FLOWER_POT_ITEM"))
			configIds.set("FLOWER_POT_ITEM", 390);
		if(!configIds.contains("CARROT_ITEM"))
			configIds.set("CARROT_ITEM", 391);
		if(!configIds.contains("POTATO_ITEM"))
			configIds.set("POTATO_ITEM", 392);
		if(!configIds.contains("BAKED_POTATO"))
			configIds.set("BAKED_POTATO", 393);
		if(!configIds.contains("POISONOUS_POTATO"))
			configIds.set("POISONOUS_POTATO", 394);
		if(!configIds.contains("EMPTY_MAP"))
			configIds.set("EMPTY_MAP", 395);
		if(!configIds.contains("GOLDEN_CARROT"))
			configIds.set("GOLDEN_CARROT", 396);
		if(!configIds.contains("SKULL_ITEM"))
			configIds.set("SKULL_ITEM", 397);
		if(!configIds.contains("CARROT_STICK"))
			configIds.set("CARROT_STICK", 398);
		if(!configIds.contains("NETHER_STAR"))
			configIds.set("NETHER_STAR", 399);
		if(!configIds.contains("PUMPKIN_PIE"))
			configIds.set("PUMPKIN_PIE", 400);
		if(!configIds.contains("FIREWORK"))
			configIds.set("FIREWORK", 401);
		if(!configIds.contains("FIREWORK_CHARGE"))
			configIds.set("FIREWORK_CHARGE", 402);
		if(!configIds.contains("ENCHANTED_BOOK"))
			configIds.set("ENCHANTED_BOOK", 403);
		if(!configIds.contains("REDSTONE_COMPARATOR"))
			configIds.set("REDSTONE_COMPARATOR", 404);
		if(!configIds.contains("NETHER_BRICK_ITEM"))
			configIds.set("NETHER_BRICK_ITEM", 405);
		if(!configIds.contains("QUARTZ"))
			configIds.set("QUARTZ", 406);
		if(!configIds.contains("EXPLOSIVE_MINECART"))
			configIds.set("EXPLOSIVE_MINECART", 407);
		if(!configIds.contains("HOPPER_MINECART"))
			configIds.set("HOPPER_MINECART", 408);
		if(!configIds.contains("IRON_BARDING"))
			configIds.set("IRON_BARDING", 417);
		if(!configIds.contains("GOLD_BARDING"))
			configIds.set("GOLD_BARDING", 418);
		if(!configIds.contains("DIAMOND_BARDING"))
			configIds.set("DIAMOND_BARDING", 419);
		if(!configIds.contains("LEASH"))
			configIds.set("LEASH", 420);
		if(!configIds.contains("NAME_TAG"))
			configIds.set("NAME_TAG", 421);
		if(!configIds.contains("COMMAND_MINECART"))
			configIds.set("COMMAND_MINECART", 422);
		if(!configIds.contains("GOLD_RECORD"))
			configIds.set("GOLD_RECORD", 2256);
		if(!configIds.contains("GREEN_RECORD"))
			configIds.set("GREEN_RECORD", 2257);
		if(!configIds.contains("RECORD_3"))
			configIds.set("RECORD_3", 2258);
		if(!configIds.contains("RECORD_4"))
			configIds.set("RECORD_4", 2259);
		if(!configIds.contains("RECORD_5"))
			configIds.set("RECORD_5", 2260);
		if(!configIds.contains("RECORD_6"))
			configIds.set("RECORD_6", 2261);
		if(!configIds.contains("RECORD_7"))
			configIds.set("RECORD_7", 2262);
		if(!configIds.contains("RECORD_8"))
			configIds.set("RECORD_8", 2263);
		if(!configIds.contains("RECORD_9"))
			configIds.set("RECORD_9", 2264);
		if(!configIds.contains("RECORD_10"))
			configIds.set("RECORD_10", 2265);
		if(!configIds.contains("RECORD_11"))
			configIds.set("RECORD_11", 2266);
		if(!configIds.contains("RECORD_12"))
			configIds.set("RECORD_12", 2267);
		if(!configIds.contains("SPONGE"))
			configIds.set("SPONGE", 19);//TODO: Banner maybe and armor stand
		//TODO: Fix any incorrect names names found out into materialnames.java
		if(!configIds.contains("SLIME"))//buy       sell
			configIds.set("SLIME", 165);//248.13     186.1
		if(!configIds.contains("BARRIER"))
			configIds.set("BARRIER", 166);
		if(!configIds.contains("IRON_TRAPDOOR"))
			configIds.set("IRON_TRAPDOOR", 167);//642.08     481.56
		if(!configIds.contains("PRISMARINE"))
			configIds.set("PRISMARINE", 168);//106.96     80.22
		if(!configIds.contains("SEA_LANTERN"))
			configIds.set("SEA_LANTERN", 169);//199.81     149.86
		if(!configIds.contains("RED_SANDSTONE"))//12.0     9.0
			configIds.set("RED_SANDSTONE", 179);
		if(!configIds.contains("RED_SANDSTONE_STAIRS"))//18.0     13.5
			configIds.set("RED_SANDSTONE_STAIRS", 180);
		if(!configIds.contains("STONE_SLAB2"))//6.0     4.5
			configIds.set("STONE_SLAB2", 180);
		if(!configIds.contains("SPRUCE_FENCE_GATE"))//30.56     22.92
			configIds.set("SPRUCE_FENCE_GATE", 183);
		if(!configIds.contains("BIRCH_FENCE_GATE"))//30.56     22.92
			configIds.set("BIRCH_FENCE_GATE", 184);
		if(!configIds.contains("JUNGLE_FENCE_GATE"))//30.56     22.92
			configIds.set("JUNGLE_FENCE_GATE", 185);
		if(!configIds.contains("DARK_OAK_FENCE_GATE"))//30.56     22.92
			configIds.set("DARK_OAK_FENCE_GATE", 186);
		if(!configIds.contains("ACACIA_FENCE_GATE"))//30.56     22.92
			configIds.set("ACACIA_FENCE_GATE", 187);
		if(!configIds.contains("SPRUCE_FENCE"))//11.46     8.6
			configIds.set("SPRUCE_FENCE", 188);
		if(!configIds.contains("BIRCH_FENCE"))//11.46     8.6
			configIds.set("BIRCH_FENCE", 189);
		if(!configIds.contains("JUNGLE_FENCE"))//11.46     8.6
			configIds.set("JUNGLE_FENCE", 190);
		if(!configIds.contains("DARK_OAK_FENCE"))//11.46     8.6
			configIds.set("DARK_OAK_FENCE", 191);
		if(!configIds.contains("ACACIA_FENCE"))//11.46     8.6
			configIds.set("ACACIA_FENCE", 192);
		if(!configIds.contains("PRISMARINE_SHARD"))
			configIds.set("PRISMARINE_SHARD", 409);//26.74     20.06
		if(!configIds.contains("PRISMARINE_CRYSTAL"))
			configIds.set("PRISMARINE_CRYSTAL", 410);//18.57     13.93
		if(!configIds.contains("RAW_RABBIT"))
			configIds.set("RAW_RABBIT", 411);//62.34     46.76
		if(!configIds.contains("COOKED_RABBIT"))
			configIds.set("COOKED_RABBIT", 412);//68.99     51.74
		if(!configIds.contains("RABBIT_STEW"))
			configIds.set("RABBIT_STEW", 413);//114.11     85.58
		if(!configIds.contains("RABBIT_FOOT"))
			configIds.set("RABBIT_FOOT", 414);//59.98     44.99
		if(!configIds.contains("RABBIT_HIDE"))
			configIds.set("RABBIT_HIDE", 415);//5.34     4.01
		if(!configIds.contains("ARMOR_STAND"))
			configIds.set("ARMOR_STAND", 416);//22.92     17.19 CHECK
		if(!configIds.contains("RAW_MUTTON"))
			configIds.set("RAW_MUTTON", 423);//62.34     46.76
		if(!configIds.contains("COOKED_MUTTON"))
			configIds.set("COOKED_MUTTON", 424);//68.99     51.74
		if(!configIds.contains("BANNER"))
			configIds.set("BANNER", 425);//107.74     80.81
		if(!configIds.contains("SPRUCE_DOOR"))
			configIds.set("SPRUCE_DOOR", 427);//15.28     11.46
		if(!configIds.contains("BIRCH_DOOR"))
			configIds.set("BIRCH_DOOR", 428);//15.28     11.46
		if(!configIds.contains("JUNGLE_DOOR"))
			configIds.set("JUNGLE_DOOR", 429);//15.28     11.46
		if(!configIds.contains("ACACIA_DOOR"))
			configIds.set("ACACIA_DOOR", 430);//15.28     11.46
		if(!configIds.contains("DARK_OAK_DOOR"))
			configIds.set("DARK_OAK_DOOR", 431);//15.28     11.46
		try {
			configIds.save(configFileIds);
		} catch(Exception e){}
	}
}