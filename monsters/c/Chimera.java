/**
 * 
 */
package com.dndcombat.monsters.c;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.ConeBreath;

/**
 
 */
public class Chimera extends Player {
 
	private static final long serialVersionUID = 1L;

	public Chimera() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Chimera");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Chimera.webp");

	    str(19).dex(11).con(19).inte(3).wis(14).cha(10);

		// Saving Throws
		strSave(+4).dexSave(+0).conSave(+4).intSave(-4).wisSave(+2).chaSave(+0);
	
		 // Combat Stats
		ac(14).init(+0).hp(114).speed(30).flySpeed(60).cr(6);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesmonstrosity);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Ram(), Bite(), Claw()));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Ram(), Bite(), FireBreath()));

        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));

        addAction(new Action("Claw", RefList.actiontypesmainaction)
                .addAttack(Claw()));

        addAction(new Action("Ram", RefList.actiontypesmainaction)
                .addAttack(Ram()));
 
	}

 
	private Attack Bite() {
	
        Attack a = new Attack("Bite"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

	private Attack Claw() {
	
        Attack a = new Attack("Claw"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

	private Attack Ram() {
	
        Attack a = new Attack("Ram"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(12) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
	
	private Attack FireBreath() {
	
        Attack a = new Attack("Fire Breath"); 
    	BreathWeapon bw = BreathWeapon.builder().actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Fire Breath")
				.recharge(5)
				.shape("Cone")
				.coneOrSphereSizeFeet(15)
				.damage(Arrays.asList(new DamageModel().nbrDice(7).dieType(8).damageTypeRefId(RefList.damagetypesfire)))
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(15)
				.build();
    	a.setSpell(new ConeBreath(bw));
        
        return a;
    }
	
}

