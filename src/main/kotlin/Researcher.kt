import Industry.*
import Modifier.*
import Rarity.*
import java.math.BigDecimal

enum class Researcher(
	val researcherName: String,
	val industry: Industry,
	val resource: String,
	val modifier: Modifier,
	val rarity: Rarity,
	val unlockedAtRank: Int
) {
	RingoRinglet("Ringo Ringlet", Potato, "Farmers", Speed, Common, 1),
	CommuneOfDoggone("Commune of Doggone", Potato, "Communes", Speed, Common, 1),
	MadMadMarx("Mad Mad Marx", Potato, "Freights", Speed, Common, 3),
	RickyJay("Ricky Jay", Potato, "Plantations", Speed, Common, 5),
	DrQuinnyForeman("Dr. Quinny Foreman", Potato, "Irrigations", Speed, Common, 8),
	DoggBenson("Dogg Benson", Potato, "Greenhouses", Speed, Common, 15),
	ErnieAbeNormal("Ernie Abe Normal", Potato, "Barges", Speed, Common, 28),
	IsaacCulkin("Isaac Culkin", Potato, "Cold Storages", Speed, Common, 42),
	NatePolly("Nate Polly", Potato, "Crop Dusters", Speed, Common, 57),
	SpudlyDoRight("Spudly Do Right", Potato, "Biodomes", Speed, Common, 80),
	UncleSmurf("Uncle Smurf", Potato, "Sky Farms", Speed, Common, 103),
	//???("???", Potato, "Potato T12s", Speed, Common, 132),

	MillyTheMighty("Milly the Mighty", Land, "Workers", Speed, Common, 3),
	BigPaulie("Big Paulie", Land, "Clearcut", Speed, Common, 4),
	DocBrownRoad("Doc Brown Road", Land, "Roads", Speed, Common, 5),
	JohnnyT("Johnny T", Land, "Sewers", Speed, Common, 7),
	JoviCannonball("Jovi Cannonball", Land, "Coal Plants", Speed, Common, 13),
	ChuckClampall("Chuck Clampall", Land, "Trains", Speed, Common, 23),
	SailorBonny("Sailor Bonny", Land, "Harbours", Speed, Common, 36),
	GeriSpringerBouvier("Geri Springer Bouvier", Land, "Nuclear Plants", Speed, Common, 51),
	MaverickRyback("Maverick Ryback", Land, "Airports", Speed, Common, 68),
	JimmyAstroseed("Jimmy Astroseed", Land, "Tube Travels", Speed, Common, 88),
	AlbertMutanstein("Albert Mutanstein", Land, "Fusion Plants", Speed, Common, 114),
	//???("???", Land, "Land T12s", Speed, Common, 144),

	DerekKeeblander("Derek Keeblander", Ore, "Miners", Speed, Common, 6),
	MorticianMarley("Mortician Marley", Ore, "Excavators", Speed, Common, 7),
	BlastOffBuds("Blast Off Buds", Ore, "Blasting Sites", Speed, Common, 9),
	Speluffy("Speluffy", Ore, "Mines", Speed, Common, 10),
	Pumbob("Pumbob", Ore, "Deep Bores", Speed, Common, 21),
	LtMcRhodey("Lt. McRhodey", Ore, "Oil Rigs", Speed, Common, 33),
	CaptainDDrumpf("Captain D. Drumpf", Ore, "Tankers", Speed, Common, 48),
	DragoactiveMan("Dragoactive Man", Ore, "Uranium Mines", Speed, Common, 64),
	LandyCalrussian("Landy Calrussian", Ore, "Ozone Collectors", Speed, Common, 84),
	ComradeBroski("Comrade Broski", Ore, "Laser Drills", Speed, Common, 108),
	//???("???" Ore, "Ore T11s" Speed, Common	 138),

	DukeOHazzard("Duke O'Hazzard", Military, "Soldiers", Speed, Common, 11),
	ChuckManhart("Chuck Manhart", Military, "Barracks", Speed, Common, 12),
	BradBones("Brad Bones", Military, "Convoys", Speed, Common, 14),
	Starvin("Starvin", Military, "Bunkers", Speed, Common, 16),
	JeanCRico("Jean C. Rico", Military, "Tanks", Speed, Common, 31),
	SkeezyMcScott("Skeezy McScott", Military, "Artilleries", Speed, Common, 45),
	CaptainJSpaddock("Captain J. Spaddock", Military, "Battleships", Speed, Common, 60),
	DominicsAngels("Dominics Angels", Military, "Fortresses", Speed, Common, 76),
	MannyOsbomb("Manny Osbomb", Military, "Bombers", Speed, Common, 98),
	LocutusFreak("Locutus Freak", Military, "Cyborgs", Speed, Common, 126),

	KennieWhooser("Kennie Whooser", Placebo, "Nurses", Speed, Common, 18),
	Patcheye("Patcheye", Placebo, "Clinics", Speed, Common, 19),
	JDMD("J.D.M.D.", Placebo, "Ambulances", Speed, Common, 20),
	HowlinMac("Howlin Mac", Placebo, "Pharmacies", Speed, Common, 25),
	EleanorLynn("Eleanor Lynn", Placebo, "Blood Banks", Speed, Common, 39),
	DoctorMcSizzly("Doctor McSizzly", Placebo, "Hospitals", Speed, Common, 54),
	DoctorHasselberg("Doctor Hasselberg", Placebo, "Coast Guards", Speed, Common, 72),
	TheKillMeNows("The Kill Me Nows", Placebo, "Laboratories", Speed, Common, 93),
	ZapMcNicoy("Zap McNicoy", Placebo, "Air Rescues", Speed, Common, 120),
	TheMonstourage("The Monstourage", Placebo, "Cloning Labs", Speed, Common, 150),

	Mactuber("Mactuber", Potato, "All Potato", Power, Rare, 3),
	TheConfectioners("The Confectioners", Potato, "All Potato", Chance, Rare, 3),
	AssistantToTheMajor("Assistant 'to the' Major", Potato, "All Potato", Trade, Rare, 3),
	JasonVanDriessen("Jason Van Driessen", Land, "All Land", Power, Rare, 3),
	TheGroovers("The Groovers", Land, "All Land", Chance, Rare, 3),
	WackoJoker("Wacko Joker", Land, "All Land", Trade, Rare, 3),
	RobertScratchesSaki("Robert Scratches Saki", Ore, "All Ore", Power, Rare, 8),
	GimliYosemiteSam("Gimli Yosemite Sam", Ore, "All Ore", Chance, Rare, 11),
	Salvadorville("Salvadorville", Ore, "All Ore", Trade, Rare, 10),
	JohnnyORambo("Johnny O. Rambo", Military, "All Military", Power, Rare, 12),
	DarthWallace("Darth Wallace", Military, "All Military", Chance, Rare, 17),
	AdrianXVultor("Adrian X. Vultor", Military, "All Military", Trade, Rare, 16),
	MerthaJoy("Mertha Joy", Placebo, "All Placebo", Power, Rare, 18),
	NurseTemple("Nurse Temple", Placebo, "All Placebo", Chance, Rare, 24),
	SirLanceEgon("Sir Lance Egon", Placebo, "All Placebo", Trade, Rare, 20),
	Magdonut("Magdonut", Potato, "Farmers", SinglePower, Rare, 34),
	TheShrink("The Shrink", Land, "Workers", SinglePower, Rare, 44),
	Thollum("Thollum", Ore, "Miners", SinglePower, Rare, 56),
	DingDongUn("Ding Dong Un", Military, "Soldiers", SinglePower, Rare, 70),
	MisaValentine("Misa Valentine", Placebo, "Nurses", SinglePower, Rare, 90),

	Pringlett("Pringlett", Potato, "All Potato", Discount, Epic, 3),
	MonsieurFrites("Monsieur Frites", Potato, "All Potato", Bonus, Epic, 3),
	JarJMusk("Jar J. Musk", Land, "All Land", Discount, Epic, 3),
	HeckAxe("Heck Axe", Land, "All Land", Bonus, Epic, 4),
	MinecraftThrain("Minecraft Thrain", Ore, "All Ore", Discount, Epic, 9),
	BigBlender("Big Blender", Ore, "All Ore", Bonus, Epic, 14),
	Megacop("Megacop", Military, "All Military", Discount, Epic, 13),
	AnnMunition("Ann Munition", Military, "All Military", Bonus, Epic, 17),
	NurseWretched("Nurse Wretched", Placebo, "All Placebo", Discount, Epic, 19),
	HannibalTavius("Hannibal Tavius", Placebo, "All Placebo", Bonus, Epic, 22),

	EarthWyrmJym("Earth Wyrm Jym", All, "All Industries", Trade, Supreme, 3),
	RatchemusPrime("Ratchemus Prime", All, "All Industries", Power, Supreme, 3),
	DrShortstack("Dr. Shortstack", All, "All Industries", Chance, Supreme, 6),
	MegaTron("Mega Tron", All, "All Industries", Discount, Supreme, 15),
	AlfStark("Alf Stark", All, "All Industries", Bonus, Supreme, 26);

	override fun toString(): String {
		return "$researcherName ($industry $modifier)"
	}

	fun getLevel() = researcherLevels[this] ?: 0

	fun getAvailableCards() = availableCards[this]?.toBigDecimal()

	fun analyze() = when (modifier) {
		Trade -> TradeAnalysis(this)
		else -> PowerAnalysis(this)
	}

	companion object {
		internal val researcherLevels = getResearcherLevels()
		private val availableCards = getAvailableCards()

		fun getAllByIndustry(industry: Industry, includeSupremes: Boolean = false) =
			values().filter { it.industry == industry || (includeSupremes && it.industry == All) }
	}
}

enum class Industry(val tradeMultiplier: BigDecimal) {
	Potato(BigDecimal(1)),
	Land(BigDecimal(2)),
	Ore(BigDecimal(3)),
	Military(BigDecimal(4)),
	Placebo(BigDecimal(5)),
	All(BigDecimal((1..5).sum()));

	override fun toString() =
		if (this == All) "Supreme" else super.toString()
}

enum class Rarity {
	Common,
	Rare,
	Epic,
	Supreme
}

enum class Modifier {
	Speed,
	Power,
	Trade,
	Chance,
	Discount,
	Bonus,
	SinglePower
}
