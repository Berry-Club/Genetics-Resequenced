package dev.aaronhowser.mods.geneticsresequenced.datagen.patchouli

class PatchouliBookReference(
	val saveName: String
) {

	val localSaveName: String
		get() = saveName.substringAfterLast('/')

}
