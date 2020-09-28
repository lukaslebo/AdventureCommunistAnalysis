import Researcher.*

fun getResearcherLevels(): Map<Researcher, Int?> {
	val researcherLevels = HashMap<Researcher, Int?>()

	// @formatter:off

	// Potaty Industry Commons
	researcherLevels[RingoRinglet]        = 6
	researcherLevels[CommuneOfDoggone]    = 4
	researcherLevels[MadMadMarx]          = 4
	researcherLevels[RickyJay]            = 4
	researcherLevels[DrQuinnyForeman]     = 4
	researcherLevels[DoggBenson]          = 4
	researcherLevels[ErnieAbeNormal]      = 5
	researcherLevels[IsaacCulkin]         = 6
	researcherLevels[NatePolly]           = null
	researcherLevels[SpudlyDoRight]       = null
	researcherLevels[UncleSmurf]          = null

	// Land Industry Commons
	researcherLevels[MillyTheMighty]      = 6
	researcherLevels[BigPaulie]           = 5
	researcherLevels[DocBrownRoad]        = 5
	researcherLevels[JohnnyT]             = 5
	researcherLevels[JoviCannonball]      = 5
	researcherLevels[ChuckClampall]       = 5
	researcherLevels[SailorBonny]         = 5
	researcherLevels[GeriSpringerBouvier] = null
	researcherLevels[MaverickRyback]      = null
	researcherLevels[JimmyAstroseed]      = null
	researcherLevels[AlbertMutanstein]    = null

	// Ore Industry Commons
	researcherLevels[DerekKeeblander]     = 5
	researcherLevels[MorticianMarley]     = 5
	researcherLevels[BlastOffBuds]        = 5
	researcherLevels[Speluffy]            = 5
	researcherLevels[Pumbob]              = 5
	researcherLevels[LtMcRhodey]          = 5
	researcherLevels[CaptainDDrumpf]      = null
	researcherLevels[DragoactiveMan]      = null
	researcherLevels[LandyCalrussian]     = null
	researcherLevels[ComradeBroski]       = null

	// Military Industry Commons
	researcherLevels[DukeOHazzard]        = 6
	researcherLevels[ChuckManhart]        = 5
	researcherLevels[BradBones]           = 5
	researcherLevels[Starvin]             = 5
	researcherLevels[JeanCRico]           = 5
	researcherLevels[SkeezyMcScott]       = null
	researcherLevels[CaptainJSpaddock]    = null
	researcherLevels[DominicsAngels]      = null
	researcherLevels[MannyOsbomb]         = null
	researcherLevels[LocutusFreak]        = null

	// Placebo Industry Commons
	researcherLevels[KennieWhooser]       = 6
	researcherLevels[Patcheye]            = 5
	researcherLevels[JDMD]                = 5
	researcherLevels[HowlinMac]           = 5
	researcherLevels[EleanorLynn]         = 6
	researcherLevels[DoctorMcSizzly]      = null
	researcherLevels[DoctorHasselberg]    = null
	researcherLevels[TheKillMeNows]       = null
	researcherLevels[ZapMcNicoy]          = null
	researcherLevels[TheMonstourage]      = null

	// Potato Rares - Power, SinglePower, Chance, Trade
	researcherLevels[Mactuber]            = 5
	researcherLevels[Magdonut]            = 3
	researcherLevels[TheConfectioners]    = 4
	researcherLevels[AssistantToTheMajor] = 5

	// Land Rares - Power, SinglePower, Chance, Trade
	researcherLevels[JasonVanDriessen]    = 5
	researcherLevels[TheShrink]           = null
	researcherLevels[TheGroovers]         = 3
	researcherLevels[WackoJoker]          = 5

	// Ore Rares - Power, SinglePower, Chance, Trade
	researcherLevels[RobertScratchesSaki] = 6
	researcherLevels[Thollum]             = null
	researcherLevels[GimliYosemiteSam]    = 3
	researcherLevels[Salvadorville]       = 5

	// Military Rares - Power, SinglePower, Chance, Trade
	researcherLevels[JohnnyORambo]        = 5
	researcherLevels[DingDongUn]          = null
	researcherLevels[DarthWallace]        = 3
	researcherLevels[AdrianXVultor]       = 8

	// Placebo Rares - Power, SinglePower, Chance, Trade
	researcherLevels[MerthaJoy]           = 5
	researcherLevels[MisaValentine]       = null
	researcherLevels[NurseTemple]         = 2
	researcherLevels[SirLanceEgon]        = 5

	// Potato Epics - Discount, Bonus
	researcherLevels[Pringlett]           = 2
	researcherLevels[MonsieurFrites]      = 4

	// Land Epics - Discount, Bonus
	researcherLevels[JarJMusk]            = 2
	researcherLevels[HeckAxe]             = 3

	// Ore Epics - Discount, Bonus
	researcherLevels[MinecraftThrain]     = 2
	researcherLevels[BigBlender]          = 3

	// Military Epics - Discount, Bonus
	researcherLevels[Megacop]             = 2
	researcherLevels[AnnMunition]         = 3

	// Placebo Epics - Discount, Bonus
	researcherLevels[NurseWretched]       = 2
	researcherLevels[HannibalTavius]      = 3

	// Supremes
	researcherLevels[RatchemusPrime]      = 1
	researcherLevels[MegaTron]            = 1
	researcherLevels[AlfStark]            = 2
	researcherLevels[EarthWyrmJym]        = 2
	researcherLevels[DrShortstack]        = null

	// @formatter:on

	return researcherLevels.toMap()
}

fun getAvailableCards(): Map<Researcher, Int> {
	val supremeCards = HashMap<Researcher, Int>()

	// @formatter:off
	supremeCards[RatchemusPrime]      = 1
	supremeCards[MegaTron]            = 1
	supremeCards[AlfStark]            = 1
	supremeCards[EarthWyrmJym]        = 0
	supremeCards[DrShortstack]        = 0
	// @formatter:on
	return supremeCards.toMap()
}
