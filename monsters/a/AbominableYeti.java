/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.DurationType.WhoseTurn;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.ConeBreath;
import com.dndcombat.monster.actions.WithinXFeetEffect;

/**
 
 */
public class AbominableYeti extends Player {
 
	private static final long serialVersionUID = 1L;

	public AbominableYeti() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Abominable Yeti");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Abominable Yeti.webp");

	    str(24).dex(10).con(22).inte(9).wis(13).cha(9);

		// Saving Throws
		strSave(+7).dexSave(+0).conSave(+6).intSave(-1).wisSave(+1).chaSave(-1);
	
		 // Combat Stats
		ac(15).init(+4).hp(137).speed(40).cr(9);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesmonstrosity);

		damageImmune(RefList.damagetypescold);
	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(ChillingGaze(), Claw(), Claw() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(ChillingGaze(), IceThrow(), IceThrow() ));

        addAction(new Action("Claw", RefList.actiontypesmainaction)
                .addAttack(Claw()));
        
        addAction(new Action("Ice Throw", RefList.actiontypesmainaction)
                .addAttack(IceThrow()));
        
        addAction(new Action("Cold Beath", RefList.actiontypesmainaction).addAttack(coldBreath()));

	}
    
    private Attack coldBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Cold Breath")
				.recharge(6)
				.shape("Cone")
				.coneOrSphereSizeFeet(30)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(10)
						.dieType(8)
						.damageTypeRefId(RefList.damagetypescold)
				))
				.saveVsRefId(RefList.savingthrowvsconstitution)
				.saveDC(18)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Cold Breath");
		a.setSpell(new ConeBreath(bw));
		return a;
	}
    
    
	private Attack ChillingGaze() {

		Attack a = new Attack("Chilling Gaze");

		SaveDC save = new SaveDC().dc(18).saveVsRefId(RefList.savingthrowvsconstitution).conditionRefId(RefList.conditionsparalyzed);

		DurationType duration = new DurationType(0);
		duration.whoseTurn = WhoseTurn.TargetTurn;
		duration.whenToEnd = WhenToEnd.EndOfTurn;
		
		WithinXFeetEffect w = new WithinXFeetEffect(30, a.getAttackName(), Arrays.asList(
				new DamageModel().nbrDice(6).dieType(6).damageTypeRefId(RefList.damagetypescold)
				.setFailConditionRefId(RefList.conditionsparalyzed).setFailSaveDuration(duration)
				),
				save, 0);
		w.maxNbrTargets = 1;

		a.setSpell(w);
		return a;
	}
		
    


	private Attack Claw() {
	
        Attack a = new Attack("Claw"); 
        a.setAttackBonus(11) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(7) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack IceThrow() {
        Attack a = new Attack("Ice Throw"); 
        a.setAttackBonus(11) 
         .setAttackRange(60) 
         .setLongRange(240) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(4) 
         .bonus(7) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
         
        // a.setThrownRange(60, 240); 
        return a;
    }

}

