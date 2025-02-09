/**
 * 
 */
package com.dndcombat.monsters.b;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Bandit extends Player {
 
	private static final long serialVersionUID = 1L;

	public Bandit() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Bandit");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Bandit.webp");

	    str(11).dex(12).con(12).inte(10).wis(10).cha(10);

		// Saving Throws
		strSave(0).dexSave(1).conSave(1).intSave(0).wisSave(0).chaSave(0);
	
		 // Combat Stats
		ac(12).init(1).hp(11).speed(30).cr(0.125);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);
  
        // Actions
        addAction(new Action("Scimitar", RefList.actiontypesmainaction)
                .addAttack(Scimitar()));
        addAction(new Action("Light Crossbow", RefList.actiontypesmainaction)
                .addAttack(LightCrossbow()));
        
        // Bonus actions
        // TODO teleport
    }
    

	private Attack Scimitar() {
		
        Attack a = new Attack("Scimitar");
        a.setAttackBonus(3)
         .setMeleereach(5)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(6)
         .bonus(1)
         .damageTypeRefId(RefList.damagetypesslashing);
        
        return a;
    }
	

	private Attack LightCrossbow() {
        Attack a = new Attack("Light Crossbow");
        a.setAttackBonus(3)
        .setAttackRange(80)
        .setLongRange(320)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(8)
         .bonus(1)
         .damageTypeRefId(RefList.damagetypespiercing);
        
        return a;
    }
	
	 
}
