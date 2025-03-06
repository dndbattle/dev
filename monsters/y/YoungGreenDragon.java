/**
 * 
 */
package com.dndcombat.monsters.y;

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
public class YoungGreenDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public YoungGreenDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Young Green Dragon");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Young Green Dragon.webp");

	    str(19).dex(12).con(17).inte(16).wis(13).cha(15);

		// Saving Throws
		strSave(+4).dexSave(+4).conSave(+3).intSave(+3).wisSave(+4).chaSave(+2);
	
		 // Combat Stats
		ac(18).init(+4).hp(136).speed(40).flySpeed(80).cr(8);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesdragon);

		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionspoisoned);
	/*
	- Multiattack for melee and ranged
	- addDamage on each attack
	
	- saving throws ending on the targets next turn have a duration of 0
	- saving throws triggered on hit can be added to the action (duration customized)
	- save or damage/effect is WithinXFeetEffect w = new WithinXFeetEffect(5, a.getAttackName(), damage, save, 0); see gladiator. 1/2 dmg on save is not the default
	
	- legendary action type and cost need to be set if you have legendary actions
	- legendary action Within spell needs to be set on the action for some reason
	*/
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(),Rend(),Rend() ));


        addAction(new Action("Poison Breath", RefList.actiontypesmainaction)
                .addAttack(PoisonBreath()));

        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
	}
    
    private Attack PoisonBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Poison Breath")
				.recharge(5)
				.shape("Cone")
				.coneOrSphereSizeFeet(30)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(12)
						.dieType(6)
						.damageTypeRefId(RefList.damagetypespoison)
				))
				.saveVsRefId(RefList.savingthrowvsconstitution)
				.saveDC(14)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Poison Breath");
		a.setSpell(new ConeBreath(bw));
		return a;
	}


	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(7) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

