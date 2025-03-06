/**
 * 
 */
package com.dndcombat.monsters.a;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.DurationType.WhoseTurn;
import com.dndcombat.fight.model.Modifier;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.ConeBreath;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.spells.bard.Fear;
import com.dndcombat.spells.basic.SpellSlot;

/**
 
 */
public class AncientWhiteDragon extends Player {
 
	private static final long serialVersionUID = 1L;

	public AncientWhiteDragon() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Ancient White Dragon");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Ancient White Dragon.webp");

	    str(26).dex(10).con(26).inte(10).wis(13).cha(18);

		// Saving Throws
		strSave(+8).dexSave(+6).conSave(+8).intSave(+0).wisSave(+7).chaSave(+4);
	
		 // Combat Stats
		ac(20).init(+12).hp(333).speed(40).flySpeed(80).cr(20);

        // Size and Type
	    size(RefList.creaturesizesgargantuan).type(RefList.creaturetypesdragon);

		getCreature().setLegendaryResistances(4);
		getCreature().setLegendaryActions(3);

		damageImmune(RefList.damagetypescold);

	      
        addAction(new Action("Cold Breath", RefList.actiontypesmainaction)
                .addAttack(coldBreath()));
        
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(), Rend(), Rend() ));

        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
        
        addAction(new Action("Pounce", RefList.actiontypeslegendaryaction)
                .addAttack(Rend() ));

        addAction(freezingBurst());
        addAction(frightfulPresence());
	}
    
	private Action frightfulPresence() {
		Action action = new Action("Frightful Presence", RefList.actiontypeslegendaryaction);
		action.setMonsterSpell(new Fear());
		return action;
	}
	
    private Action freezingBurst() {
		Action a = new Action("Freezing Burst", RefList.actiontypeslegendaryaction);
		a.setMaxUsesPerRound(1);
		a.setLegendaryCost(1);
		SaveDC save = new SaveDC();
		save.dc = 20;
		save.saveVsRefId = RefList.savingthrowvsconstitution;
		//
		DurationType duration = new DurationType(1);
		duration.whenToEnd = WhenToEnd.EndOfTurn;
		duration.whoseTurn = WhoseTurn.TargetTurn;
		ModifierType mt = new DefaultModifierType(a.getActionName()) {
	 
			private static final long serialVersionUID = 1L;

			@Override
			public boolean applyModifierOnMeStartOfMyTurnRemoveIfTrue(Player me, Modifier m, GameState state) {
				me.getStatus().setMovementLeft(0);
				state.addActionLog(me.getName() + " speed is 0 due to " + a.getActionName());
				return false;
			}
		};
		WithinXFeetEffect w = new WithinXFeetEffect(120, a.getActionName(), 
				Arrays.asList(new DamageModel().nbrDice(4).dieType(6).damageTypeRefId(RefList.damagetypescold)
						.applyModifierOnFail(mt, duration)
						),
				save, 1);
		w.maxNbrTargets = 1;

		a.setSpell(w, new SpellSlot(0));
		return a;
	}

	private Attack coldBreath() {
        // Build the breath weapon
		BreathWeapon bw = BreathWeapon.builder()
				.actionTypeRefId(RefList.actiontypesmainaction)
				.breathName("Cold Breath")
				.recharge(5)
				.shape("Cone")
				.coneOrSphereSizeFeet(90)
				.damage(Arrays.asList(
					new DamageModel()
						.nbrDice(14)
						.dieType(8)
						.damageTypeRefId(RefList.damagetypesacid)
				))
				.saveVsRefId(RefList.savingthrowvsconstitution)
				.saveDC(22)
				.build();

        // Wrap it in an Attack
		Attack a = new Attack("Cold Breath");
		a.setSpell(new ConeBreath(bw));
		return a;
	}
	
	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(14) 
         .setMeleereach(15) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(8) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

