/**
 * 
 */
package com.dndcombat.monsters.m;

import java.util.Arrays;

import com.dndcombat.actions.TargetManager;
import com.dndcombat.combat.strategies.HealerWizardStrategy;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Location;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.messaging.ClientBrowser;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.monster.traits.MagicResistance;
import com.dndcombat.monster.traits.Parry;
import com.dndcombat.spells.basic.RangeInFeet;

/**
 
 */
public class Marilith extends Player {
 
	private static final long serialVersionUID = 1L;

	public Marilith() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Marilith");
//   update creatures set active_ind = 1 where player_ind = 0 and active_ind = 0 and creature_name = 'Marilith'; 
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Marilith.webp");

	    str(18).dex(20).con(20).inte(18).wis(16).cha(20);

		// Saving Throws
		strSave(+9).dexSave(+5).conSave(+10).intSave(+4).wisSave(+8).chaSave(+10);
	
		 // Combat Stats
		ac(16).init(+10).hp(220).speed(40).cr(16);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesfiend);

		damageResist(RefList.damagetypescold);
		damageResist(RefList.damagetypesfire);
		damageResist(RefList.damagetypeslightning);
		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionspoisoned);
		getCreature().addModifierType(new MagicResistance());

	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(PactBlade(),PactBlade(),PactBlade(),PactBlade(),PactBlade(),PactBlade(), Constrict() ));

        addAction(new Action("Pact Blade", RefList.actiontypesmainaction)
                .addAttack(PactBlade()));
        
        getCreature().addModifierType(new Parry(5));
        
        getCreature().addModifierType(teleport());
        
	}

    private Attack Constrict() {
    	Attack a = new Attack("Constrict");
    	SaveDC save = new SaveDC().dc(17).saveVsRefId(RefList.savingthrowvsstrength);
    	WithinXFeetEffect w = new WithinXFeetEffect(5, a.getAttackName(), Arrays.asList(
    			new DamageModel().nbrDice(2).dieType(10).bonus(4).setFailConditionRefId(RefList.conditionsgrappledandrestrained)), save, 0);
    	w.maxNbrTargets = 1;
    	a.setSpell(w);
    	return a;
    }

	private Attack PactBlade() {
	
        Attack a = new Attack("Pact Blade"); 
        a.setAttackBonus(10) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(10) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
	
	private ModifierType teleport() {
    	return new DefaultModifierType("Teleport") {
    		 
			private static final long serialVersionUID = 1L;
        	@Override
        	public void applyModifierTypeOnMeStartOfMyTurn(Player me, GameState state) {
        		if (!me.hasBonusAction()) {
        			return;
        		}
        		Player healerOrWizard = new TargetManager().getPriorityEnemy(me, state, new RangeInFeet(120), new HealerWizardStrategy());
        		if (healerOrWizard == null && !state.enemyNextToMe(me)) {
        			// just teleport wherever if no enemy next to you
        			healerOrWizard = new TargetManager().getNearestEnemy(me, state);
        		}
        		if (healerOrWizard != null && !me.isNextTo(healerOrWizard)) {
            		Location nextTo  = healerOrWizard.getLocationNextToMeFor(me, state);
            		if (nextTo != null) {
    		            me.setLocation(nextTo);
    		            state.addActionLog(getName() + " teleports to (" + nextTo.xpos() + ", " + nextTo.ypos() + ")");
    		            me.useBonusAction();
    					ClientBrowser.updateLocation(me, state);
            		}

        		}
        	}
        };
    }

}

