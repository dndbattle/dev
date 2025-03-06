/**
 * 
 */
package com.dndcombat.monsters.g;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.ConeBreath;
import com.dndcombat.spells.basic.SpellSlot;

/**
 
 */
public class Ghost extends Player {
 
	private static final long serialVersionUID = 1L;

	public Ghost() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Ghost");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Ghost.webp");

	    str(7).dex(13).con(10).inte(10).wis(12).cha(17);

		// Saving Throws
		strSave(-2).dexSave(+1).conSave(+0).intSave(+0).wisSave(+1).chaSave(+3);
	
		 // Combat Stats
		ac(11).init(+1).hp(45).speed(5).flySpeed(40).cr(4);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		damageResist(RefList.damagetypescold);
		damageResist(RefList.damagetypesfire);
		damageResist(RefList.damagetypeslightning);
		damageResist(RefList.damagetypesthunder);
		damageResist(RefList.damagetypesslashing);
		damageResist(RefList.damagetypespiercing);
		damageResist(RefList.damagetypesbludgeoning);
		damageResist(RefList.damagetypesacid);
		damageImmune(RefList.damagetypesnecrotic);
		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionsrestrained);
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
		conditionImmune(RefList.conditionsprone);
		
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(WitheringTouch(), WitheringTouch() ));

        //TODO possession
        addAction(new Action("Withering Touch", RefList.actiontypesmainaction)
                .addAttack(WitheringTouch()));
        
        addAction(horrificVisage());
	}
    
    private Action horrificVisage() {
    	//TODO immune on save
    	Action action = new Action("Horrific Visage", RefList.actiontypesmainaction);
	       	BreathWeapon bw = BreathWeapon.builder().actionTypeRefId(RefList.actiontypesmainaction)
	   				.breathName(action.getActionName())
	   				.recharge(0)
	   				.shape("Cone")
	   				.coneOrSphereSizeFeet(60)
	   				.damage(Arrays.asList(new DamageModel().setFailConditionRefId(RefList.conditionsfrightened)
	   						.nbrDice(2).dieType(6).bonus(3).damageTypeRefId(RefList.damagetypespsychic)))
	   				.saveVsRefId(RefList.savingthrowvswisdom)
	   				.saveDC(13)
	   				.build();
	       	action.setSpell(new ConeBreath(bw).zeroDamageOnSave(), new SpellSlot(0));
	       	action.setMaxNbrUsesPerBattle(1);
           return action;
           
    }


	private Attack WitheringTouch() {
	
        Attack a = new Attack("Withering Touch"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(10) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesnecrotic); 
        
        return a;
    }

}

