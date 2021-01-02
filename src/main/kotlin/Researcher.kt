import Generator.*
import Industry.*
import Modifier.*
import Rarity.*
import java.math.BigDecimal

enum class Researcher(
	val researcherName: String,
	val industry: Industry,
	val generator: Generator?,
	val modifier: Modifier,
	val rarity: Rarity,
	val unlockedAtRank: Int
) {
	RingoRinglet("Ringo Ringlet", Potato, Farmers, Speed, Common, 1),
	CommuneOfDoggone("Commune of Doggone", Potato, Communes, Speed, Common, 1),
	MadMadMarx("Mad Mad Marx", Potato, Freights, Speed, Common, 3),
	RickyJay("Ricky Jay", Potato, Plantations, Speed, Common, 5),
	DrQuinnyForeman("Dr. Quinny Foreman", Potato, Irrigations, Speed, Common, 8),
	DoggBenson("Dogg Benson", Potato, Greenhouses, Speed, Common, 15),
	ErnieAbeNormal("Ernie Abe Normal", Potato, Barges, Speed, Common, 28),
	IsaacCulkin("Isaac Culkin", Potato, ColdStorages, Speed, Common, 42),
	NatePolly("Nate Polly", Potato, CropDusters, Speed, Common, 57),
	SpudlyDoRight("Spudly Do Right", Potato, Biodomes, Speed, Common, 80),
	UncleSmurf("Uncle Smurf", Potato, SkyFarm, Speed, Common, 103),

	MillyTheMighty("Milly the Mighty", Land, Workers, Speed, Common, 3),
	BigPaulie("Big Paulie", Land, Clearcuts, Speed, Common, 4),
	DocBrownRoad("Doc Brown Road", Land, Road, Speed, Common, 5),
	JohnnyT("Johnny T", Land, Sewer, Speed, Common, 7),
	JoviCannonball("Jovi Cannonball", Land, CoalPlant, Speed, Common, 13),
	ChuckClampall("Chuck Clampall", Land, Train, Speed, Common, 23),
	SailorBonny("Sailor Bonny", Land, Harbour, Speed, Common, 36),
	GeriSpringerBouvier("Geri Springer Bouvier", Land, NuclearPlant, Speed, Common, 51),
	MaverickRyback("Maverick Ryback", Land, Airport, Speed, Common, 68),
	JimmyAstroseed("Jimmy Astroseed", Land, TubeTravel, Speed, Common, 88),
	AlbertMutanstein("Albert Mutanstein", Land, FusionPlant, Speed, Common, 114),

	DerekKeeblander("Derek Keeblander", Ore, Miner, Speed, Common, 6),
	MorticianMarley("Mortician Marley", Ore, Excavator, Speed, Common, 7),
	BlastOffBuds("Blast Off Buds", Ore, BlastingSite, Speed, Common, 9),
	Speluffy("Speluffy", Ore, Mine, Speed, Common, 10),
	Pumbob("Pumbob", Ore, DeepBore, Speed, Common, 21),
	LtMcRhodey("Lt. McRhodey", Ore, OilRig, Speed, Common, 33),
	CaptainDDrumpf("Captain D. Drumpf", Ore, Tanker, Speed, Common, 48),
	DragoactiveMan("Dragoactive Man", Ore, UraniumMine, Speed, Common, 64),
	LandyCalrussian("Landy Calrussian", Ore, OzoneCollector, Speed, Common, 84),
	ComradeBroski("Comrade Broski", Ore, LaserDrill, Speed, Common, 108),

	DukeOHazzard("Duke O'Hazzard", Military, Soldier, Speed, Common, 11),
	ChuckManhart("Chuck Manhart", Military, Barrack, Speed, Common, 12),
	BradBones("Brad Bones", Military, Convoy, Speed, Common, 14),
	Starvin("Starvin", Military, Bunker, Speed, Common, 16),
	JeanCRico("Jean C. Rico", Military, Tank, Speed, Common, 31),
	SkeezyMcScott("Skeezy McScott", Military, Artillery, Speed, Common, 45),
	CaptainJSpaddock("Captain J. Spaddock", Military, Battleship, Speed, Common, 60),
	DominicsAngels("Dominics Angels", Military, Fortress, Speed, Common, 76),
	MannyOsbomb("Manny Osbomb", Military, Bomber, Speed, Common, 98),
	LocutusFreak("Locutus Freak", Military, Cyborg, Speed, Common, 126),

	KennieWhooser("Kennie Whooser", Placebo, Nurse, Speed, Common, 18),
	Patcheye("Patcheye", Placebo, Clinic, Speed, Common, 19),
	JDMD("J.D.M.D.", Placebo, Ambulance, Speed, Common, 20),
	HowlinMac("Howlin Mac", Placebo, Pharmacy, Speed, Common, 25),
	EleanorLynn("Eleanor Lynn", Placebo, BloodBank, Speed, Common, 39),
	DoctorMcSizzly("Doctor McSizzly", Placebo, Hospital, Speed, Common, 54),
	DoctorHasselberg("Doctor Hasselberg", Placebo, CoastGuard, Speed, Common, 72),
	TheKillMeNows("The Kill Me Nows", Placebo, Laboratory, Speed, Common, 93),
	ZapMcNicoy("Zap McNicoy", Placebo, AirRescue, Speed, Common, 120),
	TheMonstourage("The Monstourage", Placebo, CloningLab, Speed, Common, 150),

	Mactuber("Mactuber", Potato, null, Power, Rare, 3),
	TheConfectioners("The Confectioners", Potato, null, Chance, Rare, 3),
	AssistantToTheMajor("Assistant 'to the' Major", Potato, null, Trade, Rare, 3),
	JasonVanDriessen("Jason Van Driessen", Land, null, Power, Rare, 3),
	TheGroovers("The Groovers", Land, null, Chance, Rare, 3),
	WackoJoker("Wacko Joker", Land, null, Trade, Rare, 3),
	RobertScratchesSaki("Robert Scratches Saki", Ore, null, Power, Rare, 8),
	GimliYosemiteSam("Gimli Yosemite Sam", Ore, null, Chance, Rare, 11),
	Salvadorville("Salvadorville", Ore, null, Trade, Rare, 10),
	JohnnyORambo("Johnny O. Rambo", Military, null, Power, Rare, 12),
	DarthWallace("Darth Wallace", Military, null, Chance, Rare, 17),
	AdrianXVultor("Adrian X. Vultor", Military, null, Trade, Rare, 16),
	MerthaJoy("Mertha Joy", Placebo, null, Power, Rare, 18),
	NurseTemple("Nurse Temple", Placebo, null, Chance, Rare, 24),
	SirLanceEgon("Sir Lance Egon", Placebo, null, Trade, Rare, 20),
	Magdonut("Magdonut", Potato, null, SinglePower, Rare, 34),
	TheShrink("The Shrink", Land, null, SinglePower, Rare, 44),
	Thollum("Thollum", Ore, null, SinglePower, Rare, 56),
	DingDongUn("Ding Dong Un", Military, null, SinglePower, Rare, 70),
	MisaValentine("Misa Valentine", Placebo, null, SinglePower, Rare, 90),

	Pringlett("Pringlett", Potato, null, Discount, Epic, 3),
	MonsieurFrites("Monsieur Frites", Potato, null, Bonus, Epic, 3),
	JarJMusk("Jar J. Musk", Land, null, Discount, Epic, 3),
	HeckAxe("Heck Axe", Land, null, Bonus, Epic, 4),
	MinecraftThrain("Minecraft Thrain", Ore, null, Discount, Epic, 9),
	BigBlender("Big Blender", Ore, null, Bonus, Epic, 14),
	Megacop("Megacop", Military, null, Discount, Epic, 13),
	AnnMunition("Ann Munition", Military, null, Bonus, Epic, 17),
	NurseWretched("Nurse Wretched", Placebo, null, Discount, Epic, 19),
	HannibalTavius("Hannibal Tavius", Placebo, null, Bonus, Epic, 22),

	EarthWyrmJym("Earth Wyrm Jym", All, null, Trade, Supreme, 3),
	RatchemusPrime("Ratchemus Prime", All, null, Power, Supreme, 3),
	DrShortstack("Dr. Shortstack", All, null, Chance, Supreme, 6),
	MegaTron("Mega Tron", All, null, Discount, Supreme, 15),
	AlfStark("Alf Stark", All, null, Bonus, Supreme, 26);

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
