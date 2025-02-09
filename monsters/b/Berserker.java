package com.dndcombat.monsters.b;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.BloodiedFrenzy;

/**
 
 */
public class Berserker extends Player {
 
	private static final long serialVersionUID = 1L;

	public Berserker() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Berserker");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Berserker.webp");

	    str(16).dex(12).con(17).inte(9).wis(11).cha(9);

		// Saving Throws
		strSave(+3).dexSave(+1).conSave(+3).intSave(-1).wisSave(+0).chaSave(-1);
	
		 // Combat Stats
		ac(13).init(+1).hp(67).speed(30).cr(2);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);
	    
        addAction(new Action("Greataxe", RefList.actiontypesmainaction)
                .addAttack(Greataxe()));

        getCreature().addModifierType(new BloodiedFrenzy());
	}

	private Attack Greataxe() {
	    Attack a = new Attack("Greataxe");
        a.setAttackBonus(5)
         .setMeleereach(5)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(12)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypesslashing);
        
        return a;
	}
}
