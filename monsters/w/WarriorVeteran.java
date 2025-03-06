/**
 * 
 */
package com.dndcombat.monsters.w;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.Parry;

/**
 
 */
public class WarriorVeteran extends Player {
 
	private static final long serialVersionUID = 1L;

	public WarriorVeteran() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Warrior Veteran");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Veteran.webp");

	    str(16).dex(13).con(14).inte(10).wis(11).cha(10);

		// Saving Throws
		strSave(+3).dexSave(+1).conSave(+2).intSave(+0).wisSave(+0).chaSave(+0);
	
		 // Combat Stats
		ac(17).init(+3).hp(65).speed(30).cr(3);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Greatsword() , Greatsword()));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( HeavyCrossbow() , HeavyCrossbow()));

        getCreature().addModifierType(new Parry(2));

        addAction(new Action("Greatsword", RefList.actiontypesmainaction)
                .addAttack(Greatsword()));
        addAction(new Action("Heavy Crossbow", RefList.actiontypesmainaction)
                .addAttack(HeavyCrossbow()));

	}


	private Attack Greatsword() {
	
        Attack a = new Attack("Greatsword"); 
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
    private Attack HeavyCrossbow() {
        Attack a = new Attack("Heavy Crossbow"); 
        a.setAttackBonus(3) 
         .setAttackRange(100) 
         .setLongRange(400) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(10) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
         
        // a.setThrownRange(100, 400); 
        return a;
    }

}

