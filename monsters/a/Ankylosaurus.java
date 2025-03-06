/**
 * 
 */
package com.dndcombat.monsters.a;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Ankylosaurus extends Player {
 
	private static final long serialVersionUID = 1L;

	public Ankylosaurus() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Ankylosaurus");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Ankylosaurus.webp");

	    str(19).dex(11).con(15).inte(2).wis(12).cha(5);

		// Saving Throws
		strSave(+6).dexSave(+0).conSave(+2).intSave(-4).wisSave(+1).chaSave(-3);
	
		 // Combat Stats
		ac(15).init(+0).hp(68).speed(30).cr(3);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesbeast);
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Tail(), Tail()));


        addAction(new Action("Tail", RefList.actiontypesmainaction)
                .addAttack(Tail()));
	}


	private Attack Tail() {
	
        Attack a = new Attack("Tail"); 
        a.setAttackBonus(6) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(10) 
         .bonus(4)
         .autoApplyCondition(RefList.conditionsprone, new DurationType(10))
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

}

