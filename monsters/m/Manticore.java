/**
 * 
 */
package com.dndcombat.monsters.m;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Manticore extends Player {
 
	private static final long serialVersionUID = 1L;

	public Manticore() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Manticore");
//  
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Manticore.webp");

	    str(17).dex(16).con(17).inte(7).wis(12).cha(8);

		// Saving Throws
		strSave(+3).dexSave(+3).conSave(+3).intSave(-2).wisSave(+1).chaSave(-1);
	
		 // Combat Stats
		ac(14).init(+3).hp(68).speed(30).flySpeed(50).cr(3);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesmonstrosity);
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(),Rend(),Rend() ));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(TailSpike(),TailSpike(),TailSpike() ));


        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
       addAction(new Action("Tail Spike", RefList.actiontypesmainaction)
                .addAttack(TailSpike()));

	}


	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack TailSpike() {
        Attack a = new Attack("Tail Spike"); 
        a.setAttackBonus(5) 
         .setAttackRange(100) 
         .setLongRange(200) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
         
        // a.setThrownRange(100, 200); 
        return a;
    }

}

