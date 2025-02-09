/**
 * 
 */
package com.dndcombat.monsters.g;

import com.dndcombat.actions.AttackRollManager;
import com.dndcombat.actions.DamageManager;
import com.dndcombat.actions.ModifierManager;
import com.dndcombat.fight.model.AdvantageModel;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Location;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class GoblinBoss extends Player {
 
	private static final long serialVersionUID = 1L;

	public GoblinBoss() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Goblin Boss");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Goblin Boss.webp");

	    str(10).dex(15).con(10).inte(10).wis(8).cha(10);

		// Saving Throws
		strSave(+0).dexSave(+2).conSave(+0).intSave(+0).wisSave(-1).chaSave(+0);
	
		 // Combat Stats
		ac(17).init(+2).hp(21).speed(30).cr(1);

        // Size and Type
	    size(RefList.creaturesizessmall).type(RefList.creaturetypesgoblinoid);


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( ));


        addAction(new Action("Scimitar", RefList.actiontypesmainaction)
                .addAttack(Scimitar()));
        addAction(new Action("Shortbow", RefList.actiontypesmainaction)
                .addAttack(Shortbow()));
        
        getCreature().addModifierType(RedirectAttack());
        getCreature().addModifierType(GoblinWarrior.damageOnAdv());

	}


	private Attack Scimitar() {
	
        Attack a = new Attack("Scimitar"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack Shortbow() {
        Attack a = new Attack("Shortbow"); 
        a.setAttackBonus(4) 
         .setAttackRange(80) 
         .setLongRange(320) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        // If you wish to store thrown range, do something like:
        // a.setThrownRange(80, 320); 
        return a;
    }
    
    private ModifierType RedirectAttack() {
		DefaultModifierType mt = new DefaultModifierType("Redirect Attack") {
  
			private static final long serialVersionUID = 1L;

			@Override
			public AttackResult targetModifierTypeAlternativeAttackRoll(Player me, Action action, Attack attack, Player goblinboss, GameState state, AdvantageModel advantage, int attackBonus, int armorClass, AttackSettings settings) {
				if (!goblinboss.hasReaction()) {
					return null;
				}
				Player goblin = null;
				for (Player p : state.getAlliesWithinSquares(goblinboss, 1, false)) {
					if (p.isNextTo(goblinboss) && p.isAlive() && p.getName().toLowerCase().contains("goblin")) {
						goblin = p;
						Location bosslocation = goblinboss.getLocation();
						Location goblinlocation = p.getLocation();
						goblin.setLocation(bosslocation);
						goblinboss.setLocation(goblinlocation);
						break;
					}
				}
				if (goblin != null) {
					state.addActionLog(goblinboss.getName() + " redirects the attack, swapping places with " + goblin.getName());
					goblinboss.useReaction();
					AttackResult result = new AttackRollManager().roll(me, action, attack, goblin, state, advantage, attackBonus, armorClass, settings);
					if (result.hits) {
						state.addActionLog(me.getName() + " "+ result.getRollDisplay() + " hitting AC " + result.armorClass + " with "  + attack.getAttackName(), state);
						new DamageManager().rollAndApplyDamage(me, action, attack, goblin, state, settings, result, null);
					} else {
						state.addActionLog(me.getName() + " "+ result.getRollDisplay() + " missing AC " + result.armorClass + " with "  + attack.getAttackName(), state);
						new ModifierManager().applyModifiersOnAttackMiss(me, action, attack, goblin, state);
					}
					return result;
				}
				return null;
			}
			 
		};
		
		return mt;
	}
	

}

