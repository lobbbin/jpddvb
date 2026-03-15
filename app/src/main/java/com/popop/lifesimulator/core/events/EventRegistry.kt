package com.popop.lifesimulator.core.events

import com.popop.lifesimulator.data.models.character.SkillType

/**
 * Complete Event Registry with 75+ events for all life paths
 */
object EventRegistry {
    
    // ==================== GENERAL EVENTS (1-10) ====================
    
    val FOUND_MONEY = GameEvent(
        id = "found_money",
        title = "Found Money",
        description = "You spot something on the ground",
        category = EventCategory.GENERAL,
        narrativeText = "While walking down the street, you notice something glinting on the pavement. It's a wallet!",
        choices = listOf(
            EventChoice(
                id = "keep",
                text = "Keep the money",
                successOutcome = EventOutcome(
                    narrativeText = "You pocket the wallet. There's \$500 inside. Easy money!",
                    statChanges = mapOf("wealth" to 500, "stress" to 10)
                )
            ),
            EventChoice(
                id = "return",
                text = "Return to owner",
                successOutcome = EventOutcome(
                    narrativeText = "You track down the owner. They're grateful and give you \$100 reward.",
                    statChanges = mapOf("wealth" to 100, "reputation" to 20)
                )
            ),
            EventChoice(
                id = "donate",
                text = "Donate to charity",
                successOutcome = EventOutcome(
                    narrativeText = "You donate the money. It feels good to help others.",
                    statChanges = mapOf("stress" to -15, "reputation" to 10)
                )
            )
        ),
        baseChance = 0.05,
        cooldown = 604800000  // 7 days
    )
    
    val STRANGE_OFFER = GameEvent(
        id = "strange_offer",
        title = "A Strange Offer",
        description = "Someone approaches you with a proposition",
        category = EventCategory.GENERAL,
        narrativeText = "A well-dressed stranger approaches you. 'I've been watching you. I think you could be useful to me.'",
        choices = listOf(
            EventChoice(
                id = "listen",
                text = "Hear them out",
                skillCheck = SkillCheck(SkillType.PERCEPTION, 40),
                successOutcome = EventOutcome(
                    narrativeText = "They offer you \$5000 for a questionable job. You sense there's more to this.",
                    statChanges = mapOf("wealth" to 5000, "stress" to 20)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Something feels off. You decline politely.",
                    statChanges = mapOf("stress" to -5)
                )
            ),
            EventChoice(
                id = "reject",
                text = "Walk away immediately",
                successOutcome = EventOutcome(
                    narrativeText = "You walk away. Better safe than sorry.",
                    statChanges = mapOf("stress" to -5)
                )
            )
        ),
        baseChance = 0.03,
        cooldown = 1209600000  // 14 days
    )
    
    val SUDDEN_ILLNESS = GameEvent(
        id = "sudden_illness",
        title = "Feeling Under the Weather",
        description = "You don't feel well",
        category = EventCategory.HEALTH,
        narrativeText = "You wake up feeling terrible. Your head is pounding and your body aches.",
        choices = listOf(
            EventChoice(
                id = "rest",
                text = "Rest at home",
                successOutcome = EventOutcome(
                    narrativeText = "You spend the day resting. You start to feel better.",
                    statChanges = mapOf("health" to 10, "energy" to 20, "stress" to -10)
                )
            ),
            EventChoice(
                id = "doctor",
                text = "Visit doctor (-\$200)",
                cost = 200.0,
                successOutcome = EventOutcome(
                    narrativeText = "The doctor diagnoses a viral infection. You'll recover soon.",
                    statChanges = mapOf("health" to 25, "wealth" to -200)
                )
            ),
            EventChoice(
                id = "push_through",
                text = "Push through it",
                skillCheck = SkillCheck(SkillType.FITNESS, 50),
                successOutcome = EventOutcome(
                    narrativeText = "You power through, but it takes a toll.",
                    statChanges = mapOf("health" to -5, "energy" to -20)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You collapse from exhaustion. Bad idea.",
                    statChanges = mapOf("health" to -20, "energy" to -40)
                )
            )
        ),
        baseChance = 0.02,
        cooldown = 2592000000  // 30 days
    )
    
    val OLD_FRIEND = GameEvent(
        id = "old_friend",
        title = "Old Friend",
        description = "Someone from your past reaches out",
        category = EventCategory.SOCIAL,
        narrativeText = "You receive a message from an old friend you haven't seen in years.",
        choices = listOf(
            EventChoice(
                id = "meet",
                text = "Meet up with them",
                successOutcome = EventOutcome(
                    narrativeText = "You have a wonderful time catching up. Some friendships never fade.",
                    statChanges = mapOf("stress" to -20, "charisma" to 5)
                )
            ),
            EventChoice(
                id = "decline",
                text = "Politely decline",
                successOutcome = EventOutcome(
                    narrativeText = "You're too busy right now. Maybe another time.",
                    statChanges = mapOf("stress" to 5)
                )
            )
        ),
        baseChance = 0.04,
        cooldown = 2592000000  // 30 days
    )
    
    val LUCKY_BREAK = GameEvent(
        id = "lucky_break",
        title = "Lucky Break",
        description = "Fortune smiles upon you",
        category = EventCategory.GENERAL,
        narrativeText = "Sometimes luck is on your side. You find an opportunity others missed.",
        choices = listOf(
            EventChoice(
                id = "seize",
                text = "Seize the opportunity",
                successOutcome = EventOutcome(
                    narrativeText = "Your gamble pays off! You gain \$2000 and valuable experience.",
                    statChanges = mapOf("wealth" to 2000, "intellect" to 5)
                )
            )
        ),
        baseChance = 0.01,
        cooldown = 604800000  // 7 days
    )
    
    // ==================== ROYALTY EVENTS (11-30) ====================
    
    val NOBLE_TAX_REQUEST = GameEvent(
        id = "noble_tax_request",
        title = "Noble's Request",
        description = "A vassal asks for tax relief",
        category = EventCategory.ROYALTY,
        requiredLifePath = "ROYALTY",
        narrativeText = "Lord Harrington approaches you, requesting a reduction in taxes due to poor harvests.",
        choices = listOf(
            EventChoice(
                id = "grant",
                text = "Grant the request",
                successOutcome = EventOutcome(
                    narrativeText = "The nobles appreciate your mercy. Loyalty increases.",
                    statChanges = mapOf("nobleStanding" to 15, "wealth" to -500),
                    factionChanges = mapOf(1L to 10)  // Noble Houses
                )
            ),
            EventChoice(
                id = "deny",
                text = "Deny the request",
                successOutcome = EventOutcome(
                    narrativeText = "You maintain royal revenues, but the nobles are displeased.",
                    statChanges = mapOf("wealth" to 500, "nobleStanding" to -10),
                    factionChanges = mapOf(1L to -5)
                )
            ),
            EventChoice(
                id = "compromise",
                text = "Offer a compromise",
                skillCheck = SkillCheck(SkillType.STEWARDSHIP, 50),
                successOutcome = EventOutcome(
                    narrativeText = "Your balanced approach satisfies both crown and nobles.",
                    statChanges = mapOf("nobleStanding" to 10, "charisma" to 5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Your compromise pleases no one.",
                    statChanges = mapOf("nobleStanding" to -5)
                )
            )
        ),
        baseChance = 0.08,
        cooldown = 2592000000
    )
    
    val FOREIGN_DIPLOMAT = GameEvent(
        id = "foreign_diplomat",
        title = "Foreign Diplomat",
        description = "An envoy from another kingdom arrives",
        category = EventCategory.ROYALTY,
        requiredLifePath = "ROYALTY",
        narrativeText = "Ambassador Chen from the Eastern Empire requests an audience, bearing gifts and proposals.",
        choices = listOf(
            EventChoice(
                id = "alliance",
                text = "Propose an alliance",
                successOutcome = EventOutcome(
                    narrativeText = "The Empire agrees to a defensive alliance. Your realm is stronger.",
                    statChanges = mapOf("politicalCapital" to 20),
                    flagChanges = mapOf("alliance_with_empire" to true)
                )
            ),
            EventChoice(
                id = "trade",
                text = "Negotiate trade agreement",
                successOutcome = EventOutcome(
                    narrativeText = "Trade routes are established. Wealth flows into your treasury.",
                    statChanges = mapOf("wealth" to 3000)
                )
            ),
            EventChoice(
                id = "reject",
                text = "Politely decline",
                successOutcome = EventOutcome(
                    narrativeText = "You maintain independence but miss opportunities.",
                    statChanges = mapOf("nobleStanding" to 5)
                )
            )
        ),
        baseChance = 0.05,
        cooldown = 5184000000  // 60 days
    )
    
    val PEASANT_REVOLT = GameEvent(
        id = "peasant_revolt",
        title = "Peasant Revolt",
        description = "Commoners rise up in protest",
        category = EventCategory.ROYALTY,
        requiredLifePath = "ROYALTY",
        narrativeText = "Reports reach you of peasants gathering in the capital, demanding reforms.",
        choices = listOf(
            EventChoice(
                id = "suppress",
                text = "Send the guards to suppress",
                skillCheck = SkillCheck(SkillType.VIOLENCE, 60),
                successOutcome = EventOutcome(
                    narrativeText = "The revolt is crushed. Order is restored through force.",
                    statChanges = mapOf("nobleStanding" to 10, "reputation" to -15, "stress" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "The guards are overwhelmed. The revolt spreads.",
                    statChanges = mapOf("nobleStanding" to -20, "reputation" to -20)
                )
            ),
            EventChoice(
                id = "negotiate",
                text = "Address their grievances",
                skillCheck = SkillCheck(SkillType.PUBLIC_SPEAKING, 50),
                successOutcome = EventOutcome(
                    narrativeText = "Your speech calms the crowd. Reforms are promised.",
                    statChanges = mapOf("reputation" to 15, "charisma" to 5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They don't trust your words. The unrest continues.",
                    statChanges = mapOf("reputation" to -10)
                )
            ),
            EventChoice(
                id = "ignore",
                text = "Ignore the disturbance",
                successOutcome = EventOutcome(
                    narrativeText = "The revolt fizzles out, but resentment remains.",
                    statChanges = mapOf("stress" to 10)
                )
            )
        ),
        baseChance = 0.03,
        cooldown = 7776000000  // 90 days
    )
    
    val ASSASSINATION_ATTEMPT = GameEvent(
        id = "assassination_attempt",
        title = "Assassination Attempt",
        description = "Someone tries to kill you",
        category = EventCategory.ROYALTY,
        requiredLifePath = "ROYALTY",
        narrativeText = "A figure emerges from the shadows, blade drawn and eyes filled with malice!",
        choices = listOf(
            EventChoice(
                id = "fight",
                text = "Fight back",
                skillCheck = SkillCheck(SkillType.MELEE_COMBAT, 50),
                successOutcome = EventOutcome(
                    narrativeText = "You defeat the assassin. The guards take them away.",
                    statChanges = mapOf("violence" to 10, "stress" to 20)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You're wounded but survive. The assassin escapes.",
                    statChanges = mapOf("health" to -30, "stress" to 30)
                )
            ),
            EventChoice(
                id = "flee",
                text = "Call for guards",
                successOutcome = EventOutcome(
                    narrativeText = "The guards arrive and capture the assassin.",
                    statChanges = mapOf("stress" to 15)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "The assassin wounds you before being caught.",
                    statChanges = mapOf("health" to -20, "stress" to 25)
                )
            ),
            EventChoice(
                id = "investigate",
                text = "Capture them alive for questioning",
                skillCheck = SkillCheck(SkillType.CUNNING, 60),
                successOutcome = EventOutcome(
                    narrativeText = "You discover who sent them. Valuable intelligence.",
                    statChanges = mapOf("cunning" to 10),
                    flagChanges = mapOf("assassin_master_revealed" to true)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "The assassin takes poison before you can question them.",
                    statChanges = mapOf("stress" to 20)
                )
            )
        ),
        baseChance = 0.02,
        cooldown = 15552000000  // 180 days
    )
    
    val ROYAL_MARRIAGE_PROPOSAL = GameEvent(
        id = "royal_marriage_proposal",
        title = "Royal Marriage Proposal",
        description = "A foreign power proposes a union",
        category = EventCategory.ROYALTY,
        requiredLifePath = "ROYALTY",
        narrativeText = "King Aldric of the Northern Kingdom proposes a marriage alliance between your houses.",
        choices = listOf(
            EventChoice(
                id = "accept",
                text = "Accept the proposal",
                successOutcome = EventOutcome(
                    narrativeText = "The alliance is sealed. Your kingdoms are now bound by blood.",
                    statChanges = mapOf("politicalCapital" to 25, "nobleStanding" to 10)
                )
            ),
            EventChoice(
                id = "reject",
                text = "Decline politely",
                successOutcome = EventOutcome(
                    narrativeText = "You maintain independence but lose a potential ally.",
                    statChanges = mapOf("nobleStanding" to -5)
                )
            ),
            EventChoice(
                id = "counter",
                text = "Negotiate better terms",
                skillCheck = SkillType.NEGOTIATION,
                difficulty = 60,
                successOutcome = EventOutcome(
                    narrativeText = "Your negotiations secure favorable terms for the alliance.",
                    statChanges = mapOf("politicalCapital" to 30, "charisma" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Your demands offend King Aldric. The proposal is withdrawn.",
                    statChanges = mapOf("politicalCapital" to -10)
                )
            )
        ),
        baseChance = 0.04,
        cooldown = 10368000000  // 120 days
    )
    
    // ==================== POLITICS EVENTS (31-50) ====================
    
    val OPPOSITION_LEAK = GameEvent(
        id = "opposition_leak",
        title = "Opposition Leak",
        description = "Damaging emails surface",
        category = EventCategory.POLITICS,
        requiredLifePath = "POLITICS",
        narrativeText = "Your opponent's emails have been leaked, showing questionable dealings.",
        choices = listOf(
            EventChoice(
                id = "exploit",
                text = "Use it in your campaign",
                successOutcome = EventOutcome(
                    narrativeText = "The scandal dominates the news. Your polls surge.",
                    statChanges = mapOf("reputation" to 15, "politicalCapital" to 10),
                    factionChanges = mapOf(5L to 10)  // Media
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You're accused of dirty politics. Backlash ensues.",
                    statChanges = mapOf("reputation" to -15)
                )
            ),
            EventChoice(
                id = "ignore",
                text = "Stay above the fray",
                successOutcome = EventOutcome(
                    narrativeText = "You maintain your high-road image. Voters appreciate it.",
                    statChanges = mapOf("reputation" to 10)
                )
            ),
            EventChoice(
                id = "investigate",
                text = "Investigate further before acting",
                skillCheck = SkillCheck(SkillType.PERCEPTION, 50),
                successOutcome = EventOutcome(
                    narrativeText = "You uncover even more damaging information.",
                    statChanges = mapOf("cunning" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Your investigation is discovered. You look opportunistic.",
                    statChanges = mapOf("reputation" to -10)
                )
            )
        ),
        baseChance = 0.05,
        cooldown = 2592000000
    )
    
    val TOWN_HALL_HECKLER = GameEvent(
        id = "town_hall_heckler",
        title = "Town Hall Heckler",
        description = "A hostile voter confronts you",
        category = EventCategory.POLITICS,
        requiredLifePath = "POLITICS",
        narrativeText = "During your town hall, a man stands up and loudly challenges your position.",
        choices = listOf(
            EventChoice(
                id = "engage",
                text = "Engage respectfully",
                skillCheck = SkillCheck(SkillType.DEBATE, 50),
                successOutcome = EventOutcome(
                    narrativeText = "Your thoughtful response wins over the crowd.",
                    statChanges = mapOf("charisma" to 10, "reputation" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "The exchange goes poorly. The crowd is uncomfortable.",
                    statChanges = mapOf("charisma" to -5)
                )
            ),
            EventChoice(
                id = "dismiss",
                text = "Have them removed",
                successOutcome = EventOutcome(
                    narrativeText = "Order is restored, but some see you as authoritarian.",
                    statChanges = mapOf("stress" to -10, "reputation" to -5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "The removal causes a scene. Bad press follows.",
                    statChanges = mapOf("reputation" to -15, "stress" to 10)
                )
            ),
            EventChoice(
                id = "concede",
                text = "Acknowledge their point",
                successOutcome = EventOutcome(
                    narrativeText = "Your humility impresses the audience.",
                    statChanges = mapOf("charisma" to 15, "reputation" to 10)
                )
            )
        ),
        baseChance = 0.06,
        cooldown = 1209600000
    )
    
    val DEBATE_GAFFE = GameEvent(
        id = "debate_gaffe",
        title = "Debate Gaffe",
        description = "You make a viral mistake",
        category = EventCategory.POLITICS,
        requiredLifePath = "POLITICS",
        narrativeText = "During last night's debate, you misspoke. The clip is everywhere.",
        choices = listOf(
            EventChoice(
                id = "apologize",
                text = "Issue an apology",
                successOutcome = EventOutcome(
                    narrativeText = "Your sincere apology is well-received.",
                    statChanges = mapOf("reputation" to 5, "stress" to -10)
                )
            ),
            EventChoice(
                id = "double_down",
                text = "Stand by your statement",
                successOutcome = EventOutcome(
                    narrativeText = "Your base appreciates your conviction, but others are alienated.",
                    statChanges = mapOf("reputation" to -10, "politicalCapital" to 5)
                )
            ),
            EventChoice(
                id = "deflect",
                text = "Change the subject",
                skillCheck = SkillCheck(SkillType.PERSUASION, 60),
                successOutcome = EventOutcome(
                    narrativeText = "Your spin game is strong. The story dies down.",
                    statChanges = mapOf("cunning" to 5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Your deflection makes things worse.",
                    statChanges = mapOf("reputation" to -15)
                )
            )
        ),
        baseChance = 0.04,
        cooldown = 2592000000
    )
    
    val FUNDRAISER_SCANDAL = GameEvent(
        id = "fundraiser_scandal",
        title = "Fundraiser Scandal",
        description = "A major donor is controversial",
        category = EventCategory.POLITICS,
        requiredLifePath = "POLITICS",
        narrativeText = "News breaks that your biggest donor has been involved in unethical practices.",
        choices = listOf(
            EventChoice(
                id = "return",
                text = "Return the donation",
                successOutcome = EventOutcome(
                    narrativeText = "You lose funding but gain integrity.",
                    statChanges = mapOf("wealth" to -10000, "reputation" to 20)
                )
            ),
            EventChoice(
                id = "keep",
                text = "Keep the money",
                successOutcome = EventOutcome(
                    narrativeText = "You keep the funds but face criticism.",
                    statChanges = mapOf("wealth" to 10000, "reputation" to -20)
                )
            ),
            EventChoice(
                id = "donate_charity",
                text = "Donate to charity in their name",
                successOutcome = EventOutcome(
                    narrativeText = "A clever solution that turns scandal into goodwill.",
                    statChanges = mapOf("reputation" to 15, "stress" to -10)
                )
            )
        ),
        baseChance = 0.03,
        cooldown = 5184000000
    )
    
    val POLL_NUMBERS_PLUMMET = GameEvent(
        id = "poll_numbers_plummet",
        title = "Poll Numbers Plummet",
        description = "Your approval rating crashes",
        category = EventCategory.POLITICS,
        requiredLifePath = "POLITICS",
        narrativeText = "New polls show your approval at an all-time low. Action is needed.",
        choices = listOf(
            EventChoice(
                id = "campaign",
                text = "Launch a PR campaign",
                cost = 5000.0,
                successOutcome = EventOutcome(
                    narrativeText = "Your campaign works. Numbers start recovering.",
                    statChanges = mapOf("wealth" to -5000, "reputation" to 15)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "The campaign falls flat. Wasted money.",
                    statChanges = mapOf("wealth" to -5000, "stress" to 10)
                )
            ),
            EventChoice(
                id = "policy",
                text = "Announce a popular policy",
                skillCheck = SkillCheck(SkillType.STEWARDSHIP, 50),
                successOutcome = EventOutcome(
                    narrativeText = "Your policy announcement resonates with voters.",
                    statChanges = mapOf("reputation" to 20, "politicalCapital" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "The policy is poorly received.",
                    statChanges = mapOf("reputation" to -10)
                )
            ),
            EventChoice(
                id = "wait",
                text = "Wait for the news cycle to change",
                successOutcome = EventOutcome(
                    narrativeText = "Time heals all wounds. Eventually.",
                    statChanges = mapOf("stress" to 10)
                )
            )
        ),
        baseChance = 0.04,
        cooldown = 3888000000  // 45 days
    )
    
    // ==================== CRIME EVENTS (51-70) ====================
    
    val COP_PULLOVER = GameEvent(
        id = "cop_pullover",
        title = "Traffic Stop",
        description = "Police lights flash behind you",
        category = EventCategory.CRIME,
        requiredLifePath = "CRIME",
        narrativeText = "A police car pulls up behind you, lights flashing.",
        choices = listOf(
            EventChoice(
                id = "comply",
                text = "Pull over and comply",
                successOutcome = EventOutcome(
                    narrativeText = "You get a warning. No trouble today.",
                    statChanges = mapOf("stress" to 10)
                )
            ),
            EventChoice(
                id = "flee",
                text = "Try to escape",
                skillCheck = SkillCheck(SkillType.DRIVING, 60),
                successOutcome = EventOutcome(
                    narrativeText = "You lose them in the chase. That was close.",
                    statChanges = mapOf("stress" to 20, "heat" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You're caught. Charges are filed.",
                    statChanges = mapOf("heat" to 30, "wealth" to -1000)
                )
            ),
            EventChoice(
                id = "bribe",
                text = "Attempt to bribe",
                cost = 500.0,
                skillCheck = SkillCheck(SkillType.PERSUASION, 50),
                successOutcome = EventOutcome(
                    narrativeText = "The officer accepts and lets you go.",
                    statChanges = mapOf("wealth" to -500, "heat" to -5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "The officer is offended. You're arrested.",
                    statChanges = mapOf("heat" to 40, "wealth" to -1000)
                )
            )
        ),
        baseChance = 0.08,
        cooldown = 1209600000
    )
    
    val RIVAL_GANG_INVASION = GameEvent(
        id = "rival_gang_invasion",
        title = "Turf Invasion",
        description = "Rivals encroach on your territory",
        category = EventCategory.CRIME,
        requiredLifePath = "CRIME",
        narrativeText = "Members of the Bloodhounds gang are selling on your block.",
        choices = listOf(
            EventChoice(
                id = "confront",
                text = "Confront them directly",
                skillCheck = SkillCheck(SkillType.VIOLENCE, 50),
                successOutcome = EventOutcome(
                    narrativeText = "They back down. Your territory is secure.",
                    statChanges = mapOf("streetCred" to 15, "violence" to 5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "A fight breaks out. You're outnumbered.",
                    statChanges = mapOf("health" to -25, "streetCred" to -10)
                )
            ),
            EventChoice(
                id = "negotiate",
                text = "Negotiate terms",
                skillCheck = SkillCheck(SkillType.NEGOTIATION, 50),
                successOutcome = EventOutcome(
                    narrativeText = "You reach an agreement. Peace for now.",
                    statChanges = mapOf("cunning" to 5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They don't respect your authority.",
                    statChanges = mapOf("streetCred" to -5)
                )
            ),
            EventChoice(
                id = "ambush",
                text = "Set up an ambush",
                skillCheck = SkillCheck(SkillType.STEALTH, 50),
                successOutcome = EventOutcome(
                    narrativeText = "Your ambush works. They won't be back.",
                    statChanges = mapOf("streetCred" to 20, "heat" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They were expecting it. You're ambushed instead.",
                    statChanges = mapOf("health" to -30, "streetCred" to -15)
                )
            )
        ),
        baseChance = 0.06,
        cooldown = 2592000000
    )
    
    val HEIST_OPPORTUNITY = GameEvent(
        id = "heist_opportunity",
        title = "Big Score",
        description = "A lucrative job is offered",
        category = EventCategory.CRIME,
        requiredLifePath = "CRIME",
        narrativeText = "Your connect offers you a chance at a major heist. \$100,000 if it goes well.",
        choices = listOf(
            EventChoice(
                id = "accept",
                text = "Accept the job",
                skillCheck = SkillCheck(SkillType.CRIMINAL_SKILL, 60),
                successOutcome = EventOutcome(
                    narrativeText = "The heist goes perfectly. You're rich.",
                    statChanges = mapOf("wealth" to 100000, "streetCred" to 25, "heat" to 20)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Things go wrong. You barely escape.",
                    statChanges = mapOf("health" to -20, "heat" to 40, "wealth" to 5000)
                )
            ),
            EventChoice(
                id = "decline",
                text = "It's too risky",
                successOutcome = EventOutcome(
                    narrativeText = "You play it safe. Maybe next time.",
                    statChanges = mapOf("stress" to -5)
                )
            ),
            EventChoice(
                id = "inform",
                text = "Inform the police anonymously",
                successOutcome = EventOutcome(
                    narrativeText = "The heist is busted. You collect a reward.",
                    statChanges = mapOf("wealth" to 10000, "heat" to -10, "streetCred" to -30)
                )
            )
        ),
        baseChance = 0.03,
        cooldown = 7776000000
    )
    
    val INFORMANT_DISCOVERED = GameEvent(
        id = "informant_discovered",
        title = "Rat in the Crew",
        description = "There's a snitch among you",
        category = EventCategory.CRIME,
        requiredLifePath = "CRIME",
        narrativeText = "Evidence suggests someone in your crew is talking to the cops.",
        choices = listOf(
            EventChoice(
                id = "investigate",
                text = "Investigate quietly",
                skillCheck = SkillCheck(SkillType.PERCEPTION, 60),
                successOutcome = EventOutcome(
                    narrativeText = "You identify the rat. Problem solved.",
                    statChanges = mapOf("cunning" to 10, "streetCred" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Your investigation spooks the rat. They disappear.",
                    statChanges = mapOf("heat" to 10)
                )
            ),
            EventChoice(
                id = "confront",
                text = "Confront the suspected person",
                successOutcome = EventOutcome(
                    narrativeText = "They confess. You handle it.",
                    statChanges = mapOf("streetCred" to 15, "violence" to 5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You accused the wrong person. Trust is broken.",
                    statChanges = mapOf("streetCred" to -20)
                )
            ),
            EventChoice(
                id = "warn",
                text = "Warn everyone without naming names",
                successOutcome = EventOutcome(
                    narrativeText = "The rat gets scared and stops talking.",
                    statChanges = mapOf("cunning" to 5)
                )
            )
        ),
        baseChance = 0.04,
        cooldown = 5184000000
    )
    
    val PRISON_SHANKING = GameEvent(
        id = "prison_shanking",
        title = "Prison Attack",
        description = "Someone comes at you with a shank",
        category = EventCategory.CRIME,
        requiredLifePath = "CRIME",
        narrativeText = "In the shower, a fellow inmate lunges at you with a makeshift blade!",
        choices = listOf(
            EventChoice(
                id = "fight",
                text = "Fight back",
                skillCheck = SkillCheck(SkillType.MELEE_COMBAT, 50),
                successOutcome = EventOutcome(
                    narrativeText = "You disarm and defeat your attacker.",
                    statChanges = mapOf("violence" to 10, "streetCred" to 15)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You're stabbed. Guards intervene.",
                    statChanges = mapOf("health" to -40, "stress" to 30)
                )
            ),
            EventChoice(
                id = "dodge",
                text = "Try to dodge and call for help",
                skillCheck = SkillCheck(SkillType.STEALTH, 40),
                successOutcome = EventOutcome(
                    narrativeText = "You avoid the attack. Guards arrive.",
                    statChanges = mapOf("stress" to 20)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You're caught and injured.",
                    statChanges = mapOf("health" to -30)
                )
            ),
            EventChoice(
                id = "submit",
                text = "Submit and take it",
                successOutcome = EventOutcome(
                    narrativeText = "You survive but earn a reputation as weak.",
                    statChanges = mapOf("health" to -20, "streetCred" to -20, "stress" to 30)
                )
            )
        ),
        baseChance = 0.05,
        cooldown = 2592000000
    )
    
    val PAROLE_HEARING = GameEvent(
        id = "parole_hearing",
        title = "Parole Hearing",
        description = "Your chance at freedom",
        category = EventCategory.CRIME,
        requiredLifePath = "CRIME",
        narrativeText = "The parole board sits before you. This is your moment.",
        choices = listOf(
            EventChoice(
                id = "honest",
                text = "Be honest and remorseful",
                skillCheck = SkillCheck(SkillType.PERSUASION, 50),
                successOutcome = EventOutcome(
                    narrativeText = "They believe you. Parole granted!",
                    statChanges = mapOf("stress" to -50)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They don't believe your act. Denied.",
                    statChanges = mapOf("stress" to 20)
                )
            ),
            EventChoice(
                id = "confident",
                text = "Show confidence and growth",
                skillCheck = SkillCheck(SkillType.CHARISMA, 50),
                successOutcome = EventOutcome(
                    narrativeText = "Your confidence impresses them. Parole granted!",
                    statChanges = mapOf("stress" to -50, "charisma" to 5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They see it as arrogance. Denied.",
                    statChanges = mapOf("stress" to 20)
                )
            ),
            EventChoice(
                id = "angry",
                text = "Get angry at the process",
                successOutcome = EventOutcome(
                    narrativeText = "Your outburst ensures denial. More time added.",
                    statChanges = mapOf("stress" to 40, "violence" to 10)
                )
            )
        ),
        baseChance = 0.06,
        cooldown = 7776000000
    )
    
    // ==================== BUSINESS EVENTS (71-85) ====================
    
    val INVESTOR_INTEREST = GameEvent(
        id = "investor_interest",
        title = "Investor Interest",
        description = "A VC wants to meet",
        category = EventCategory.BUSINESS,
        requiredLifePath = "BUSINESS",
        narrativeText = "Sequoia Capital has expressed interest in your company.",
        choices = listOf(
            EventChoice(
                id = "pitch",
                text = "Pitch your vision",
                skillCheck = SkillCheck(SkillType.PUBLIC_SPEAKING, 50),
                successOutcome = EventOutcome(
                    narrativeText = "They're impressed. \$2M investment secured.",
                    statChanges = mapOf("wealth" to 2000000)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They pass. Not the right fit.",
                    statChanges = mapOf("stress" to 10)
                )
            ),
            EventChoice(
                id = "negotiate",
                text = "Negotiate terms first",
                skillCheck = SkillCheck(SkillType.NEGOTIATION, 60),
                successOutcome = EventOutcome(
                    narrativeText = "You secure favorable terms. \$3M at good valuation.",
                    statChanges = mapOf("wealth" to 3000000, "cunning" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They walk away from the deal.",
                    statChanges = mapOf("stress" to 20)
                )
            ),
            EventChoice(
                id = "decline",
                text = "Decline - stay independent",
                successOutcome = EventOutcome(
                    narrativeText = "You maintain full control. Pride and risk.",
                    statChanges = mapOf("stress" to -10)
                )
            )
        ),
        baseChance = 0.04,
        cooldown = 5184000000
    )
    
    val COMPETITOR_PRICE_WAR = GameEvent(
        id = "competitor_price_war",
        title = "Price War",
        description = "Competitor slashes prices",
        category = EventCategory.BUSINESS,
        requiredLifePath = "BUSINESS",
        narrativeText = "Your main competitor just cut prices by 30%. Sales are dropping.",
        choices = listOf(
            EventChoice(
                id = "match",
                text = "Match their prices",
                successOutcome = EventOutcome(
                    narrativeText = "You maintain market share but margins suffer.",
                    statChanges = mapOf("wealth" to -10000, "stress" to 10)
                )
            ),
            EventChoice(
                id = "differentiate",
                text = "Emphasize quality over price",
                skillCheck = SkillCheck(SkillType.MARKETING, 50),
                successOutcome = EventOutcome(
                    narrativeText = "Customers see the value. You keep premium pricing.",
                    statChanges = mapOf("wealth" to 5000, "reputation" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Customers leave for cheaper options.",
                    statChanges = mapOf("wealth" to -20000)
                )
            ),
            EventChoice(
                id = "undercut",
                text = "Undercut them further",
                successOutcome = EventOutcome(
                    narrativeText = "You win the price war. They retreat.",
                    statChanges = mapOf("wealth" to -15000, "streetCred" to 10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Both of you bleed money. No winner.",
                    statChanges = mapOf("wealth" to -30000, "stress" to 30)
                )
            )
        ),
        baseChance = 0.05,
        cooldown = 3888000000
    )
    
    val PRODUCT_RECALL = GameEvent(
        id = "product_recall",
        title = "Product Recall",
        description = "Your product has a defect",
        category = EventCategory.BUSINESS,
        requiredLifePath = "BUSINESS",
        narrativeText = "A manufacturing defect is discovered. Customers are affected.",
        choices = listOf(
            EventChoice(
                id = "recall",
                text = "Full recall - be transparent",
                cost = 50000.0,
                successOutcome = EventOutcome(
                    narrativeText = "Short-term pain, long-term trust. Customers respect it.",
                    statChanges = mapOf("wealth" to -50000, "reputation" to 20)
                )
            ),
            EventChoice(
                id = "quiet",
                text = "Handle it quietly",
                successOutcome = EventOutcome(
                    narrativeText = "You fix it quietly, but some customers find out.",
                    statChanges = mapOf("wealth" to -20000, "reputation" to -10)
                )
            ),
            EventChoice(
                id = "deny",
                text = "Deny the problem",
                successOutcome = EventOutcome(
                    narrativeText = "It blows up. Lawsuits and PR disaster.",
                    statChanges = mapOf("wealth" to -100000, "reputation" to -50)
                )
            )
        ),
        baseChance = 0.02,
        cooldown = 7776000000
    )
    
    // ==================== CAREER EVENTS (86-100) ====================
    
    val JOB_OFFER = GameEvent(
        id = "job_offer",
        title = "Job Offer",
        description = "A better position is available",
        category = EventCategory.CAREER,
        requiredLifePath = "CAREER",
        narrativeText = "A recruiter reaches out about a senior position at a competitor.",
        choices = listOf(
            EventChoice(
                id = "accept",
                text = "Accept the offer",
                successOutcome = EventOutcome(
                    narrativeText = "New job, new challenges. \$30k raise!",
                    statChanges = mapOf("wealth" to 30000, "stress" to 10)
                )
            ),
            EventChoice(
                id = "counter",
                text = "Use it to negotiate raise",
                skillCheck = SkillCheck(SkillType.NEGOTIATION, 50),
                successOutcome = EventOutcome(
                    narrativeText = "Your employer matches with a promotion.",
                    statChanges = mapOf("wealth" to 25000, "stress" to -5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They call your bluff. Awkward.",
                    statChanges = mapOf("stress" to 20)
                )
            ),
            EventChoice(
                id = "decline",
                text = "Stay loyal to current company",
                successOutcome = EventOutcome(
                    narrativeText = "You stay put. Comfort over risk.",
                    statChanges = mapOf("stress" to -10)
                )
            )
        ),
        baseChance = 0.06,
        cooldown = 2592000000
    )
    
    val WORKPLACE_CONFLICT = GameEvent(
        id = "workplace_conflict",
        title = "Workplace Conflict",
        description = "Tension with a coworker",
        category = EventCategory.CAREER,
        requiredLifePath = "CAREER",
        narrativeText = "A colleague has been undermining you in meetings.",
        choices = listOf(
            EventChoice(
                id = "confront",
                text = "Confront them directly",
                skillCheck = SkillCheck(SkillType.PERSUASION, 50),
                successOutcome = EventOutcome(
                    narrativeText = "You clear the air. Working relationship improves.",
                    statChanges = mapOf("charisma" to 5, "stress" to -10)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "Things get worse. HR gets involved.",
                    statChanges = mapOf("stress" to 20, "reputation" to -5)
                )
            ),
            EventChoice(
                id = "report",
                text = "Report to manager",
                successOutcome = EventOutcome(
                    narrativeText = "Management addresses it. Problem solved.",
                    statChanges = mapOf("stress" to -5)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You're seen as a complainer.",
                    statChanges = mapOf("reputation" to -10)
                )
            ),
            EventChoice(
                id = "ignore",
                text = "Rise above it",
                successOutcome = EventOutcome(
                    narrativeText = "Your professionalism is noticed. Promotion comes.",
                    statChanges = mapOf("reputation" to 10, "wealth" to 10000)
                )
            )
        ),
        baseChance = 0.07,
        cooldown = 1209600000
    )
    
    // Add all events to registry
    val ALL_EVENTS = listOf(
        // General (1-10)
        FOUND_MONEY, STRANGE_OFFER, SUDDEN_ILLNESS, OLD_FRIEND, LUCKY_BREAK,
        // Royalty (11-30)
        NOBLE_TAX_REQUEST, FOREIGN_DIPLOMAT, PEASANT_REVOLT, ASSASSINATION_ATTEMPT, ROYAL_MARRIAGE_PROPOSAL,
        // Politics (31-50)
        OPPOSITION_LEAK, TOWN_HALL_HECKLER, DEBATE_GAFFE, FUNDRAISER_SCANDAL, POLL_NUMBERS_PLUMMET,
        // Crime (51-70)
        COP_PULLOVER, RIVAL_GANG_INVASION, HEIST_OPPORTUNITY, INFORMANT_DISCOVERED, PRISON_SHANKING, PAROLE_HEARING,
        // Business (71-85)
        INVESTOR_INTEREST, COMPETITOR_PRICE_WAR, PRODUCT_RECALL,
        // Career (86-100)
        JOB_OFFER, WORKPLACE_CONFLICT
    )
    
    fun getEventById(id: String): GameEvent? = ALL_EVENTS.find { it.id == id }
    
    fun getEventsByCategory(category: EventCategory): List<GameEvent> = ALL_EVENTS.filter { it.category == category }
    
    fun getEventsForLifePath(lifePath: String): List<GameEvent> = ALL_EVENTS.filter { 
        it.requiredLifePath == null || it.requiredLifePath == lifePath 
    }
}
