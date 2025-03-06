/**
 * 
 */
package com.dndcombat.monsters.v;

import java.util.Arrays;
import java.util.List;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.Charm;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.spells.cleric.Command;
 

/**
 
 */
public class Vampire extends Player {
 
	private static final long serialVersionUID = 1L;

	public Vampire() {
		super(new Creature());
		create();
    }

    /** 
     */
    public void create() {
    	getCreature().setCreatureName("Vampire");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Vampire.webp");

	    str(18).dex(18).con(18).inte(17).wis(15).cha(18);

		// Saving Throws
		strSave(+4).dexSave(+9).conSave(+9).intSave(+3).wisSave(+7).chaSave(+9);
	
		 // Combat Stats
		ac(16).init(+14).hp(195).speed(40).cr(13);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		getCreature().setLegendaryResistances(3);
		setMonsterSpellDC(17);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(GraveStrike(), GraveStrike(), Bite() )
                );

        addAction(charm());
        addAction(command());

        addAction(new Action("Grave Strike", RefList.actiontypesmainaction)
                .addAttack(GraveStrike()));

        addAction(new Action("Deathless Strike", RefList.actiontypeslegendaryaction)
                .addAttack(GraveStrike()));
        
        getCreature().addModifierType(drain());
	}
    
    private ModifierType drain() {

	  DefaultModifierType mt = new DefaultModifierType("Bite") {
        	 
			private static final long serialVersionUID = 1L;
			@Override
			public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> list, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
     		   
        		if (source.isGrappling(target)) {
            		int total = 0;
            		for (Damage d : damageSoFar) {
            			if (d.damageTypeRefId == RefList.damagetypesnecrotic) {
            				total += d.dmg;
            			}
            		}
            		if (total > 0) {
            		    state.addActionLog(source.getName() + " drains " + total);
            		    source.heal(Arrays.asList(new Damage(total, RefList.damagetypesheal)), state);
            		    target.reduceMaxHp(total, state);
               		    state.addActionLog(target.getName() + " max hp reduced to " + target.getStatus().getMaxHitPoints());
            		}
           		} else {
        			state.addActionLog(source.getName() + " unable to bite " + target.getName());;
        		}
        		return false;
        	}
        };
        return mt;
    }

	private Attack Bite() {

		Attack a = new Attack("Bite");
		a.setMustBeGrappledByMe(true);
		SaveDC save = new SaveDC();
		save.dc = 17;
		save.saveVsRefId = RefList.savingthrowvsconstitution;
		WithinXFeetEffect w = new WithinXFeetEffect(120, a.getAttackName(),
					Arrays.asList(new DamageModel().nbrDice(1).dieType(4).bonus(4),
							new DamageModel().nbrDice(3).dieType(8).damageTypeRefId(RefList.damagetypesnecrotic)),
				save, 1);
		w.maxNbrTargets = 1;

		a.setSpell(w);
		

		return a;

	}
	
	private Attack GraveStrike() {
	
        Attack a = new Attack("Grave Strike"); 
        a.setAttackBonus(9) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .setGrappleOnHit(true)
         .setGrappleDc(14)
         .setGrappleLargeOrSmaller(true)
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
    
    private Action command() {
		Action a = new Action("Command", RefList.actiontypeslegendaryaction);
		a.setMaxUsesPerRound(1);
		a.setMonsterSpell(new Command());
		return a;
	}


	private Action charm() {
		Action a = new Action("Charm", RefList.actiontypesbonusaction);
		//TODO move recharge to action
		a.setSpell(new Charm("Charm", 17, true), new SpellSlot(0));
		return a;
	}
}

