/**
 * 
 */
package com.dndcombat.monsters.t;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.PackTactics;

/**
 
 */
public class Tough extends Player {
 
	private static final long serialVersionUID = 1L;

	public Tough() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Tough");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Orc.webp");

	    str(15).dex(12).con(14).inte(10).wis(10).cha(11);

		// Saving Throws
		strSave(+2).dexSave(+1).conSave(+2).intSave(+0).wisSave(+0).chaSave(+0);
	
		 // Combat Stats
		ac(12).init(+1).hp(32).speed(30).cr(0.5);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);


        addAction(new Action("Mace", RefList.actiontypesmainaction)
                .addAttack(Mace()));
       addAction(new Action("Heavy Crossbow", RefList.actiontypesmainaction)
                .addAttack(HeavyCrossbow()));

       getCreature().addModifierType(new PackTactics());
	}


	private Attack Mace() {
	
        Attack a = new Attack("Mace"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
    private Attack HeavyCrossbow() {
        Attack a = new Attack("Heavy Crossbow"); 
        a.setAttackBonus(3) 
         .setAttackRange(100) 
         .setLongRange(400) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(10) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
         
        // a.setThrownRange(100, 400); 
        return a;
    }

}

