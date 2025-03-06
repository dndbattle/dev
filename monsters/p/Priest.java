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
import com.dndcombat.spells.cleric.SpiritGuardians;

/**
 
 */
public class Priest extends Player {
 
	private static final long serialVersionUID = 1L;

	public Priest() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Priest");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Priest.webp");

	    str(16).dex(10).con(12).inte(13).wis(16).cha(13);

		// Saving Throws
		strSave(+3).dexSave(+0).conSave(+1).intSave(+1).wisSave(+3).chaSave(+1);
	
		 // Combat Stats
		ac(13).init(+0).hp(38).speed(30).cr(2);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Mace(), Mace() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(RadiantFlame(), RadiantFlame() ));


        addAction(new Action("Mace", RefList.actiontypesmainaction)
                .addAttack(Mace()));
        
        addAction(new Action("Radiant Flame", RefList.actiontypesmainaction)
                .addAttack(RadiantFlame()));
        

        addAction(healingword());
        addAction(spiritguardians());
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

 	private Action healingword() {
 		Action a = new Action("Healing Word", RefList.actiontypesbonusaction);

 		a.setLimitedUses(2);
 		a.setHelping(true);

 		a.setMonsterSpell(new HealingWord());

 		return a;
 	}

 	private Action spiritguardians() {
 		Action a = new Action("Spirit Guardians", RefList.actiontypesmainaction);

 		a.setLimitedUses(1);
 		a.setMonsterSpell(new SpiritGuardians());

 		return a;
 	}
 	
	private Attack Mace() {
	
        Attack a = new Attack("Mace"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        a.addDamage().nbrDice(2).dieType(4).damageTypeRefId(RefList.damagetypesradiant);

        return a;
    }

}

