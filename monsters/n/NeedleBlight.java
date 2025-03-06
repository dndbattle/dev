/**
 * 
 */
package com.dndcombat.monsters.n;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class NeedleBlight extends Player {
 
	private static final long serialVersionUID = 1L;

	public NeedleBlight() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Needle Blight");
    	

	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Needle Blight.webp");

	    str(12).dex(12).con(13).inte(4).wis(8).cha(3);

		// Saving Throws
		strSave(+1).dexSave(+1).conSave(+1).intSave(-3).wisSave(-1).chaSave(-4);
	
		 // Combat Stats
		ac(12).init(+1).hp(16).speed(30).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesplant);

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
        addAction(new Action("Claw", RefList.actiontypesmainaction)
                .addAttack(Claw()));
       addAction(new Action("Needles", RefList.actiontypesmainaction)
                .addAttack(Needles()));

	}


	private Attack Claw() {
	
        Attack a = new Attack("Claw"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(4) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack Needles() {
        Attack a = new Attack("Needles"); 
        a.setAttackBonus(3) 
         .setAttackRange(30) 
         .setLongRange(60) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(4) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
         
        // a.setThrownRange(30, 60); 
        return a;
    }

}

