/**
 * 
 */
package com.dndcombat.monsters.p;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.spells.cleric.HealingWord;

/**
 
 */
public class PriestAcolyte extends Player {
 
	private static final long serialVersionUID = 1L;

	public PriestAcolyte() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Priest Acolyte");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Priest.webp");

	    str(14).dex(10).con(12).inte(10).wis(14).cha(11);

		// Saving Throws
		strSave(+2).dexSave(+0).conSave(+1).intSave(+0).wisSave(+2).chaSave(+0);
	
		 // Combat Stats
		ac(13).init(+0).hp(11).speed(30).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

	 
        addAction(new Action("Mace", RefList.actiontypesmainaction)
                .addAttack(Mace()));
        
        addAction(new Action("Radiant Flame", RefList.actiontypesmainaction)
                .addAttack(RadiantFlame()));
        
        addAction(healingword());
	}


	private Action healingword() {
		Action a = new Action("Healing Word", RefList.actiontypesbonusaction);

		a.setLimitedUses(1);
		a.setHelping(true);

		a.setMonsterSpell(new HealingWord());

		return a;
	}

	private Attack RadiantFlame() {
	
        Attack a = new Attack("Radiant Flame"); 
        a.setAttackBonus(4) 
         .setAttackRange(60) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .damageTypeRefId(RefList.damagetypesradiant); 
        
        return a;
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
        a.addDamage().nbrDice(1).dieType(4).damageTypeRefId(RefList.damagetypesradiant);
        
        return a;
    }

}

