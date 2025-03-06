/**
 * 
 */
package com.dndcombat.monsters.h;

import com.dndcombat.fight.map.DnDDistanceCalculator;
import com.dndcombat.fight.model.AdvantageModel;
import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.Parry;

/**
 
 */
public class HobgoblinWarlord extends Player {
 
	private static final long serialVersionUID = 1L;

	public HobgoblinWarlord() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Hobgoblin Warlord");
   	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Hobgoblin Warlord.webp");

	    str(17).dex(14).con(16).inte(14).wis(11).cha(15);

		// Saving Throws
		strSave(+3).dexSave(+5).conSave(+3).intSave(+5).wisSave(+3).chaSave(+5);
	
		 // Combat Stats
		ac(20).init(+5).hp(112).speed(30).cr(6);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesgoblinoid);

	/*
	- Multiattack for melee and ranged
	- addDamage on each attack
	
	- monsterSpellDc and monsterSpellAttack
	
	- saving throws ending on the targets next turn have a duration of 0
	- saving throws triggered on hit can be added to the action (duration customized)
	- save or damage/effect is WithinXFeetEffect w = new WithinXFeetEffect(5, a.getAttackName(), damage, save, 0); see gladiator. 1/2 dmg on save is not the default
	
	- legendary action type and cost need to be set if you have legendary actions
	- legendary action Within spell needs to be set on the action for some reason
	
	- healing or buff spells must be marked at the action level to be helping
	
	*/
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Longsword(),Longsword(),Longsword() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Javelin(),Javelin(),Javelin() ));


       addAction(new Action("Javelin", RefList.actiontypesmainaction)
                .addAttack(Javelin())); //TODO slow modifier


       addAction(new Action("Javelin Thrown", RefList.actiontypesmainaction)
                .addAttack(JavelinThrown()));


        addAction(new Action("Longsword", RefList.actiontypesmainaction)
                .addAttack(Longsword()));
        
        getCreature().addModifierType(new Parry(3));
        
        getCreature().addModifierType(aura());

	}
    
    private ModifierType aura() {
		return new DefaultModifierType("Aura of Authority") {
			 
			private static final long serialVersionUID = 1L;
		 
			@Override
			public void modifierTypeOnSomeoneAttackAlterAdvantage(Player someone, AdvantageModel a, Player source, Player target, Action action, Attack attack, GameState state, AttackSettings settings) {
				if (someone.isIncapacitated() || someone.isEnemy(source)) {
					return; // doesnt affect itself or non undead
				}
				if (DnDDistanceCalculator.roughDistanceInSquares(someone, source) > 2) {
					return;
				}
				a.setAdvantage(true);
				a.setAdvantageReason(modifierName);
			}
			
			@Override
			public void modifierTypeOnSomeoneAlterSaveAdvantage(Player someone, AdvantageModel a, Player source, Player meMakingSave, Action action, GameState state) {
				if (someone.isIncapacitated() || someone.isEnemy(source)) {
					return; // doesnt affect itself or non undead
				}
				if (DnDDistanceCalculator.roughDistanceInSquares(someone, source) > 2) {
					return;
				}
				a.setAdvantage(true);
				a.setAdvantageReason(modifierName);
			}
		};
	}


    private Attack Javelin() {
        Attack a = new Attack("Javelin"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }
    private Attack JavelinThrown() {
        Attack a = new Attack("Javelin Thrown"); 
        a.setAttackBonus(6) 
         .setAttackRange(30) // 0 if not found
         .setLongRange(120) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        return a;
    }

	private Attack Longsword() {
	
        Attack a = new Attack("Longsword"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

