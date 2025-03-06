/**
 * 
 */
package com.dndcombat.monsters.b;

import java.util.Arrays;
import java.util.List;

import com.dndcombat.actions.ActionManager;
import com.dndcombat.actions.MainActionAgainException;
import com.dndcombat.actions.TargetManager;
import com.dndcombat.combat.strategies.HealerWizardStrategy;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.map.Node;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Location;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.messaging.ClientBrowser;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.monster.traits.FireAura;
import com.dndcombat.monster.traits.MagicResistance;
import com.dndcombat.spells.basic.RangeInFeet;

/**
 
 */
public class Balor extends Player {
 
	private static final long serialVersionUID = 1L;

//	Next up Death Throws and Teleport (to cleric/wizard)
	
	public Balor() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Balor");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Balor.webp");

        // Ability Scores
        str(26).dex(15).con(22).inte(20).wis(16).cha(22);

        // Saving Throws
        strSave(8).dexSave(2).conSave(12).intSave(5).wisSave(9).chaSave(6);

        // Combat Stats
        ac(19).init(14).hp(287).speed(40).flySpeed(80).cr(19);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesfiend);
	    
	    // damage resistance
	    damageResist(RefList.damagetypescold, RefList.damagetypeslightning);
	    damageImmune(RefList.damagetypesfire, RefList.damagetypespoison);
	    conditionImmune(RefList.conditionscharmed, RefList.conditionsfrightened, RefList.conditionspoisoned);
	    
	    // TODO death throes
	    // fire aura
        getCreature().addModifierType(new FireAura(3,6));
        // legendary resistance
	    getCreature().setLegendaryResistances(3);
	    // magic resistance
	    getCreature().addModifierType(new MagicResistance());
	    
        // Actions
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(FlameWhip(),LightningBlade()));
    
        // Actions
        addAction(new Action("Flame Whip", RefList.actiontypesmainaction)
                .addAttack(FlameWhip()));
        addAction(new Action("Lightning Blade", RefList.actiontypesmainaction)
                .addAttack(LightningBlade()));
        
        getCreature().addModifierType(teleport());
        
        getCreature().addModifierType(deathThroes());
    }
    
    private ModifierType deathThroes() {
    	return new DefaultModifierType("Death Throes") {
    		 
			private static final long serialVersionUID = 1L;
			@Override
			public void applyModifierTypeOnMyDeath(Player me, GameState state) {
				try {
					Action explode = new Action("Death Throes", RefList.actiontypesfreeaction);
					WithinXFeetEffect w = new WithinXFeetEffect(30, explode.getActionName(), 
							Arrays.asList(new DamageModel().nbrDice(9).dieType(6).damageTypeRefId(RefList.damagetypesfire),
									new DamageModel().nbrDice(9).dieType(6).damageTypeRefId(RefList.damagetypesforce)), new SaveDC().dc(30).saveVsRefId(RefList.savingthrowvsdexterity), 0);
					explode.setMonsterSpell(w);
					List<Player> targets = state.getEnemiesWithinSquares(me, 6);
					try {
						new ActionManager().performAction(me, targets.size() == 0 ? new Player(new Node()) : targets.get(0), explode, state);
					} catch (MainActionAgainException main) {
						
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
        };
    }

    private ModifierType teleport() {
    	return new DefaultModifierType("Teleport") {
    		 
			private static final long serialVersionUID = 1L;
        	@Override
        	public void applyModifierTypeOnMeStartOfMyTurn(Player me, GameState state) {
        		if (!me.hasBonusAction()) {
        			return;
        		}
        		Player healerOrWizard = new TargetManager().getPriorityEnemy(me, state, new RangeInFeet(50), new HealerWizardStrategy());
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

	private Attack FlameWhip() {
		
        Attack a = new Attack("Flame Whip");
        a.setAttackBonus(14)
         .setMeleereach(30)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(3)
         .dieType(6)
         .bonus(8)
         .damageTypeRefId(RefList.damagetypesforce);
        a.addDamage()
        .nbrDice(5)
        .dieType(6)
        .dragNextToMe()
        .damageTypeRefId(RefList.damagetypesfire)
        .autoApplyCondition(RefList.conditionsprone, new DurationType(10))
        ;
        
        return a;
    }
	

	private Attack LightningBlade() {
		DurationType duration = new DurationType(1);
		duration.whenToEnd = WhenToEnd.StartOfTurn;
        Attack a = new Attack("Lightning Blade");
        a.setAttackBonus(14)
        .setMeleereach(10)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(3)
         .dieType(8)
         .bonus(8)
         .damageTypeRefId(RefList.damagetypesforce);
        a.addDamage()
        .nbrDice(4)
        .dieType(10)
        .autoApplyCondition(RefList.conditionsnoreactions, duration)
        .damageTypeRefId(RefList.damagetypeslightning);
        
        return a;
    }
	
	 
}
