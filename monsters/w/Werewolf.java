/**
 * 
 */
package com.dndcombat.monsters.w;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Werewolf extends Player {
 
	private static final long serialVersionUID = 1L;

	public Werewolf() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Werewolf");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Werewolf.webp");

	    str(16).dex(14).con(14).inte(10).wis(11).cha(10);

		// Saving Throws
		strSave(+3).dexSave(+2).conSave(+2).intSave(+0).wisSave(+0).chaSave(+0);
	
		 // Combat Stats
		ac(15).init(+4).hp(71).speed(30).cr(3);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesmonstrosity);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Scratch(), Bite()));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Longbow(), Longbow()));

        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));

        addAction(new Action("Scratch", RefList.actiontypesmainaction)
                .addAttack(Scratch()));
        addAction(new Action("Longbow (Humanoid or Hybrid Form Only)", RefList.actiontypesmainaction)
                .addAttack(Longbow()));

	}


    //TODO curse
	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(5) 
         .setMeleereach(5)
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

	private Attack Scratch() {
	
        Attack a = new Attack("Scratch"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack Longbow() {
        Attack a = new Attack("Longbow (Humanoid or Hybrid Form Only)"); 
        a.setAttackBonus(4) 
         .setAttackRange(150) 
         .setLongRange(600) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
         
        // a.setThrownRange(150, 600); 
        return a;
    }

}

