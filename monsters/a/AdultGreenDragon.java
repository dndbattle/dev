/**
 * 
 */
package com.dndcombat.monsters.a;

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
public class AdultGreenDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public AdultGreenDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Adult Green Dragon");
//   update creatures set active_ind = 1 where player_ind = 0 and active_ind = 0 and creature_name = 'Adult Green Dragon'; 
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Adult Green Dragon.webp");

	    str(23).dex(12).con(21).inte(18).wis(15).cha(18);

		// Saving Throws
		strSave(+6).dexSave(+6).conSave(+5).intSave(+4).wisSave(+7).chaSave(+4);
	
		 // Combat Stats
		ac(19).init(+11).hp(207).speed(40).flySpeed(80).cr(15);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesdragon);

		getCreature().setLegendaryResistances(3);
		getCreature().setLegendaryActions(3);

		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionspoisoned);
	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(),Rend(),Rend() ));

        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));

        addAction(new Action("Poison Breath", RefList.actiontypesmainaction)
                .addAttack(PoisonBreath()));
        
        addAction(new Action("Pounce", RefList.actiontypeslegendaryaction)
                .addAttack(Rend())); //TODO move up to 1/2 speed

	}

    private Attack PoisonBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Poison Breath")
				.recharge(5)
				.shape("Cone")
				.coneOrSphereSizeFeet(60)
				.damage(Arrays.asList(new DamageModel().nbrDice(16).dieType(6).damageTypeRefId(RefList.damagetypespoison)))
				.saveVsRefId(RefList.savingthrowvsconstitution)
				.saveDC(18)
				.build();
		
		Attack a = new Attack("Poison Breath");
		a.setSpell(new ConeBreath(bw));
		return a;
	}
    
	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(11) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(6) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

