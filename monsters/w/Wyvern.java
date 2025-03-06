/**
 * 
 */
package com.dndcombat.monsters.w;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Wyvern extends Player {
 
	private static final long serialVersionUID = 1L;

	public Wyvern() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Wyvern");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Wyvern.webp");

	    str(19).dex(10).con(16).inte(5).wis(12).cha(6);

		// Saving Throws
		strSave(+4).dexSave(+0).conSave(+3).intSave(-3).wisSave(+1).chaSave(-2);
	
		 // Combat Stats
		ac(14).init(+0).hp(127).speed(30).flySpeed(80).cr(6);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesdragon);


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Bite(), Sting()));

        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));

        addAction(new Action("Sting", RefList.actiontypesmainaction)
                .addAttack(Sting()));
	}


	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(4)
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }
 
	private Attack Sting() {
	
        Attack a = new Attack("Sting"); 
        a.setAttackBonus(7) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        a.addDamage().nbrDice(7).dieType(6).damageTypeRefId(RefList.damagetypespoison)
        .autoApplyCondition(RefList.conditionspoisoned, new DurationType(1));
        
        return a;
    }

}

