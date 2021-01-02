import Generator.*
import Industry.*
import Researcher.*
import Resource.*
import java.math.BigDecimal
import java.math.RoundingMode

interface Producible {
	val name: String
}

enum class Resource : Producible {
	Potatoes,
	Land,
	Ore,
	Bullets,
	Placebos
}

enum class Generator(
	val generatorName: String,
	val industry: Industry,
	val tier: Int,
	val baseProductionTime: BigDecimal,
	val baseProductionOutput: BigDecimal,
	val produces: Producible,
	val researcher: Researcher
) : Producible {
	// @formatter:off
	Farmers("Farmers", Potato, 1, TWO, BigDecimal(2 + 1), Potatoes, RingoRinglet),
	Communes("Communes", Potato, 2, TWO.pow(2), BigDecimal(2 + 2), Farmers, CommuneOfDoggone),
	Freights("Freights", Potato, 3, TWO.pow(3), BigDecimal(2 + 3), Communes, MadMadMarx),
	Plantations("Plantations", Potato, 4, TWO.pow(4), BigDecimal(2 + 4), Freights, RickyJay),
	Irrigations("Irrigations", Potato, 5, TWO.pow(5), BigDecimal(2 + 5), Plantations, DrQuinnyForeman),
	Greenhouses("Greenhouses", Potato, 6, TWO.pow(6), BigDecimal(2 + 6), Irrigations, DoggBenson),
	Barges("Barges", Potato, 7, TWO.pow(7), BigDecimal(2 + 7), Greenhouses, ErnieAbeNormal),
	ColdStorages("Cold Storages", Potato, 8, TWO.pow(8), BigDecimal(2 + 8), Barges, IsaacCulkin),
	CropDusters("Crop Dusters", Potato, 9, TWO.pow(9), BigDecimal(2 + 9), ColdStorages, NatePolly),
	Biodomes("Biodomes", Potato, 10, TWO.pow(10), BigDecimal(2 + 10), CropDusters, SpudlyDoRight),
	SkyFarm("Sky Farm", Potato, 11, TWO.pow(11), BigDecimal(2 + 11), Biodomes, UncleSmurf),

	Workers("Workers", Industry.Land, 1, BigDecimal(3), BigDecimal(3 + 1), Resource.Land, MillyTheMighty),
	Clearcuts("Clearcuts", Industry.Land, 2, BigDecimal(3).multiply(TWO), BigDecimal(3 + 2), Workers, BigPaulie),
	Road("Road", Industry.Land, 3, BigDecimal(3).multiply(TWO.pow(2)), BigDecimal(3 + 3), Clearcuts, DocBrownRoad),
	Sewer("Sewer", Industry.Land, 4, BigDecimal(3).multiply(TWO.pow(3)), BigDecimal(3 + 4), Road, JohnnyT),
	CoalPlant("Coal Plant", Industry.Land, 5, BigDecimal(3).multiply(TWO.pow(4)), BigDecimal(3 + 5), Sewer, JoviCannonball),
	Train("Train", Industry.Land, 6, BigDecimal(3).multiply(TWO.pow(5)), BigDecimal(3 + 6), CoalPlant, ChuckClampall),
	Harbour("Harbour", Industry.Land, 7, BigDecimal(3).multiply(TWO.pow(6)), BigDecimal(3 + 7), Train, SailorBonny),
	NuclearPlant("Nuclear Plant", Industry.Land, 8, BigDecimal(3).multiply(TWO.pow(7)), BigDecimal(3 + 8), Harbour, GeriSpringerBouvier),
	Airport("Airport", Industry.Land, 9, BigDecimal(3).multiply(TWO.pow(8)), BigDecimal(3 + 9), NuclearPlant, MaverickRyback),
	TubeTravel("Tube Travel", Industry.Land, 10, BigDecimal(3).multiply(TWO.pow(9)), BigDecimal(3 + 10), Airport, JimmyAstroseed),
	FusionPlant("Fusion Plant", Industry.Land, 11, BigDecimal(3).multiply(TWO.pow(10)), BigDecimal(3 + 11), Airport, AlbertMutanstein),

	Miner("Miner", Industry.Ore, 1, BigDecimal(4), BigDecimal(4 + 1), Resource.Ore, DerekKeeblander),
	Excavator("Excavator", Industry.Ore, 2, BigDecimal(4).multiply(TWO), BigDecimal(4 + 2), Miner, MorticianMarley),
	BlastingSite("Blasting Site", Industry.Ore, 3, BigDecimal(4).multiply(TWO.pow(2)), BigDecimal(4 + 3), Excavator, BlastOffBuds),
	Mine("Mine", Industry.Ore, 4, BigDecimal(4).multiply(TWO.pow(3)), BigDecimal(4 + 4), BlastingSite, Speluffy),
	DeepBore("Deep Bore", Industry.Ore, 5, BigDecimal(4).multiply(TWO.pow(4)), BigDecimal(4 + 5), Mine, Pumbob),
	OilRig("Oil Rig", Industry.Ore, 6, BigDecimal(4).multiply(TWO.pow(5)), BigDecimal(4 + 6), DeepBore, LtMcRhodey),
	Tanker("Tanker", Industry.Ore, 7, BigDecimal(4).multiply(TWO.pow(6)), BigDecimal(4 + 7), OilRig, CaptainDDrumpf),
	UraniumMine("Uranium Mine", Industry.Ore, 8, BigDecimal(4).multiply(TWO.pow(7)), BigDecimal(4 + 8), Tanker, DragoactiveMan),
	OzoneCollector("Ozone Collector", Industry.Ore, 9, BigDecimal(4).multiply(TWO.pow(8)), BigDecimal(4 + 9), UraniumMine, LandyCalrussian),
	LaserDrill("Laser Drill", Industry.Ore, 10, BigDecimal(4).multiply(TWO.pow(9)), BigDecimal(4 + 10), OzoneCollector, ComradeBroski),

	Soldier("Soldier", Military, 1, BigDecimal(5), BigDecimal(5 + 1), Bullets, DukeOHazzard),
	Barrack("Barrack", Military, 2, BigDecimal(5).multiply(TWO), BigDecimal(5 + 2), Soldier, ChuckManhart),
	Convoy("Convoy", Military, 3, BigDecimal(5).multiply(TWO.pow(2)), BigDecimal(5 + 3), Barrack, BradBones),
	Bunker("Bunker", Military, 4, BigDecimal(5).multiply(TWO.pow(3)), BigDecimal(5 + 4), Convoy, Starvin),
	Tank("Tank", Military, 5, BigDecimal(5).multiply(TWO.pow(4)), BigDecimal(5 + 5), Bunker, JeanCRico),
	Artillery("Artillery", Military, 6, BigDecimal(5).multiply(TWO.pow(5)), BigDecimal(5 + 6), Tank, SkeezyMcScott),
	Battleship("Battleship", Military, 7, BigDecimal(5).multiply(TWO.pow(6)), BigDecimal(5 + 7), Artillery, CaptainJSpaddock),
	Fortress("Fortress", Military, 8, BigDecimal(5).multiply(TWO.pow(7)), BigDecimal(5 + 8), Battleship, DominicsAngels),
	Bomber("Bomber", Military, 9, BigDecimal(5).multiply(TWO.pow(8)), BigDecimal(5 + 9), Fortress, MannyOsbomb),
	Cyborg("Cyborg", Military, 10, BigDecimal(5).multiply(TWO.pow(9)), BigDecimal(5 + 10), Bomber, LocutusFreak),

	Nurse("Nurse", Placebo, 1, BigDecimal(6), BigDecimal(6 + 1), Placebos, KennieWhooser),
	Clinic("Clinic", Placebo, 2, BigDecimal(6).multiply(TWO), BigDecimal(6 + 2), Nurse, Patcheye),
	Ambulance("Ambulance", Placebo, 3, BigDecimal(6).multiply(TWO.pow(2)), BigDecimal(6 + 3), Clinic, JDMD),
	Pharmacy("Pharmacy", Placebo, 4, BigDecimal(6).multiply(TWO.pow(3)), BigDecimal(6 + 4), Ambulance, HowlinMac),
	BloodBank("Blood Bank", Placebo, 5, BigDecimal(6).multiply(TWO.pow(4)), BigDecimal(6 + 5), Pharmacy, EleanorLynn),
	Hospital("Hospital", Placebo, 6, BigDecimal(6).multiply(TWO.pow(5)), BigDecimal(6 + 6), BloodBank, DoctorMcSizzly),
	CoastGuard("Coast Guard", Placebo, 7, BigDecimal(6).multiply(TWO.pow(6)), BigDecimal(6 + 7), Hospital, DoctorHasselberg),
	Laboratory("Laboratory", Placebo, 8, BigDecimal(6).multiply(TWO.pow(7)), BigDecimal(6 + 8), CoastGuard, TheKillMeNows),
	AirRescue("Air Rescue", Placebo, 9, BigDecimal(6).multiply(TWO.pow(8)), BigDecimal(6 + 9), Laboratory, ZapMcNicoy),
	CloningLab("Cloning Lab", Placebo, 10, BigDecimal(6).multiply(TWO.pow(9)), BigDecimal(6 + 10), AirRescue, TheMonstourage);
	// @formatter:on

	var producedBy: Generator? = null
	var productionTime: BigDecimal? = null
	var averageOutput: BigDecimal? = null
	var averageOutputPerSecond: BigDecimal? = null
	var amount = BigDecimal.ONE

	override fun toString(): String {
		return "$name(industry=${industry.name}, tier=$tier, baseProductionTime=$baseProductionTime, baseProductionOutput=$baseProductionOutput, produces=${produces.name}, producedBy=${producedBy?.name}, researcher=${researcher?.name})"
	}
}

fun initGenerators(): Boolean {
	Farmers.producedBy = Communes
	Communes.producedBy = Freights
	Freights.producedBy = Plantations
	Plantations.producedBy = Irrigations
	Irrigations.producedBy = Greenhouses
	Greenhouses.producedBy = Barges
	Barges.producedBy = ColdStorages
	ColdStorages.producedBy = CropDusters
	CropDusters.producedBy = Biodomes
	Biodomes.producedBy = SkyFarm
	SkyFarm.producedBy = null

	Workers.producedBy = Clearcuts
	Clearcuts.producedBy = Road
	Road.producedBy = Sewer
	Sewer.producedBy = CoalPlant
	CoalPlant.producedBy = Train
	Train.producedBy = Harbour
	Harbour.producedBy = NuclearPlant
	NuclearPlant.producedBy = Airport
	Airport.producedBy = TubeTravel
	TubeTravel.producedBy = FusionPlant
	FusionPlant.producedBy = null

	Miner.producedBy = Excavator
	Excavator.producedBy = BlastingSite
	BlastingSite.producedBy = Mine
	Mine.producedBy = DeepBore
	DeepBore.producedBy = OilRig
	OilRig.producedBy = Tanker
	Tanker.producedBy = UraniumMine
	UraniumMine.producedBy = OzoneCollector
	OzoneCollector.producedBy = LaserDrill
	LaserDrill.producedBy = null

	Soldier.producedBy = Barrack
	Barrack.producedBy = Convoy
	Convoy.producedBy = Bunker
	Bunker.producedBy = Tank
	Tank.producedBy = Artillery
	Artillery.producedBy = Battleship
	Battleship.producedBy = Fortress
	Fortress.producedBy = Bomber
	Bomber.producedBy = Cyborg
	Cyborg.producedBy = null

	Nurse.producedBy = Clinic
	Clinic.producedBy = Ambulance
	Ambulance.producedBy = Pharmacy
	Pharmacy.producedBy = BloodBank
	BloodBank.producedBy = Hospital
	Hospital.producedBy = CoastGuard
	CoastGuard.producedBy = Laboratory
	Laboratory.producedBy = AirRescue
	AirRescue.producedBy = CloningLab
	CloningLab.producedBy = null

	Generator.values().forEach {
		it.productionTime = it.baseProductionTime
			.divide(TWO.pow(it.researcher.getLevel()), 8, RoundingMode.HALF_UP)
			.stripTrailingZeros()
		it.averageOutput = it.baseProductionOutput
			.times(it.industry.getPowerMultiplierForIndustry())
			.times(it.industry.getBonusMultiplierForIndustry())
			.times(it.industry.getChanceForIndustry())
			.times(if (it.tier == 1) it.industry.getSinglePowerMultiplierForIndustry() else BigDecimal.ONE)
		it.averageOutputPerSecond = it.averageOutput
			?.divide(it.productionTime, 8, RoundingMode.HALF_UP)
			?.stripTrailingZeros()
	}

	return true
}

