/**
 * 
 */
package com.dndcombat.monsters.r;

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
public class RedDragonWyrmling extends Player {
 
	private static final long serialVersionUID = 1L;

	public RedDragonWyrmling() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Red Dragon Wyrmling");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Red Dragon Wyrmling.webp");

	    str(19).dex(10).con(17).inte(12).wis(11).cha(15);

		// Saving Throws
		strSave(+4).dexSave(+2).conSave(+3).intSave(+1).wisSave(+2).chaSave(+2);
	
		 // Combat Stats
		ac(17).init(+2).hp(75).speed(30).flySpeed(60).cr(4);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesdragon);

		damageImmune(RefList.damagetypesfire);
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(),Rend() ));


        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
        
        addAction(new Action("Fire Breath", RefList.actiontypesmainaction)
                .addAttack(FireBreath()));
	}

    private Attack FireBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Fire Breath")
				.recharge(5)
				.shape("Cone")
				.coneOrSphereSizeFeet(15)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(7)
						.dieType(6)
						.damageTypeRefId(RefList.damagetypesfire)
				))
				.saveVsRefId(RefList.savingthrowvsdexterity)
				.saveDC(13)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Cold Breath");
		a.setSpell(new ConeBreath(bw));
		return a;
	}

	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(10) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.addDamage().nbrDice(1).dieType(6).damageTypeRefId(RefList.damagetypesfire);
        
        return a;
    }

}

