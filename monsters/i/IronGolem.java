/**
 * 
 */
package com.dndcombat.monsters.i;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.ConeBreath;
import com.dndcombat.monster.traits.MagicResistance;

/**
 
 */
public class IronGolem extends Player {
 
	private static final long serialVersionUID = 1L;

	public IronGolem() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Iron Golem");
//   update creatures set active_ind = 1 where player_ind = 0 and active_ind = 0 and creature_name = 'Iron Golem'; 
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Iron Golem.webp");

	    str(24).dex(9).con(20).inte(3).wis(11).cha(1);

		// Saving Throws
		strSave(+7).dexSave(-1).conSave(+5).intSave(-4).wisSave(+0).chaSave(-5);
	
		 // Combat Stats
		ac(20).init(+9).hp(252).speed(30).cr(16);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesconstruct);

		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypesfire);
		damageImmune(RefList.damagetypespsychic);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
		getCreature().addModifierType(new MagicResistance());
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(BladedArm(),BladedArm() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(FieryBolt(),FieryBolt() ));

        addAction(new Action("Bladed Arm", RefList.actiontypesmainaction)
                .addAttack(BladedArm()));
        
        addAction(new Action("Fiery Bolt", RefList.actiontypesmainaction)
                .addAttack(FieryBolt()));
        
        addAction(new Action("Poison Breath", RefList.actiontypesmainaction).addAttack(PoisonBreath()));
	}

	private Attack PoisonBreath() {
		
        Attack a = new Attack("Poison Breath"); 
    	BreathWeapon bw = BreathWeapon.builder().actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Poison Breath")
				.recharge(6)
				.shape("Cone")
				.coneOrSphereSizeFeet(60)
				.damage(Arrays.asList(new DamageModel().nbrDice(10).dieType(10).damageTypeRefId(RefList.damagetypespoison)))
				.saveVsRefId(RefList.savingthrowvsconstitution)
				.saveDC(18)
				.build();
    	a.setSpell(new ConeBreath(bw));
        
        return a;
    }
	

	private Attack BladedArm() {
	
        Attack a = new Attack("Bladed Arm"); 
        a.setAttackBonus(12) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(8) 
         .bonus(7) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }


	private Attack FieryBolt() {
	
        Attack a = new Attack("Fiery Bolt"); 
        a.setAttackBonus(10) 
         .setAttackRange(120) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(8) 
         .dieType(8)
         .damageTypeRefId(RefList.damagetypesfire); 
        
        return a;
    }

}

