/**
 * 
 */
package com.dndcombat.monsters.m;

import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Modifier;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.spells.wizard.ConeOfCold;
import com.dndcombat.spells.wizard.Fireball;

/**
 
 */
public class Mage extends Player {
 
	private static final long serialVersionUID = 1L;

	public Mage() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Mage");
//   update creatures set active_ind = 1 where player_ind = 0 and active_ind = 0 and creature_name = 'Mage'; 
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Mage.webp");

	    str(9).dex(14).con(11).inte(17).wis(12).cha(11);

		// Saving Throws
		strSave(-1).dexSave(+2).conSave(+0).intSave(+6).wisSave(+4).chaSave(+0);
	
		 // Combat Stats
		ac(15).init(+2).hp(81).speed(30).cr(6);
		
		setMonsterSpellDC(14);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( ));


        addAction(new Action("Arcane Burst", RefList.actiontypesmainaction)
                .addAttack(ArcaneBurst()));

        addAction(new Action("Arcane Burst", RefList.actiontypesmainaction)
                .addAttack(ArcaneBurstRanged()));


       addAction(new Action("Arcane Burst Thrown", RefList.actiontypesmainaction)
                .addAttack(ArcaneBurstThrown()));
       
       addAction(coneOfCold());

       addAction(fireball());
       
       //TODO misty step and counterspell
       getCreature().addModifierType(shield());

	}
    
    private ModifierType shield() {
    	return new DefaultModifierType("Shield") {
    	 
			private static final long serialVersionUID = 1L;
			
			@Override
			public int getClutchArmorClassBonus(Player me, Action action, Attack attack, Player target, GameState state, int usedRoll, int attackBonus, int armorClass, AttackSettings settings) {
    			if (target.hasReaction()) {
    				if (usedRoll + attackBonus >= armorClass 
    						&& usedRoll + attackBonus -5 < armorClass) {
    					if (target.getTotalCastTimes(modifierName) < 3) {
    						state.addActionLog(target.getName() + " casts shield +5 AC");
    						target.useReaction();
    						target.markCast(modifierName);
    						DurationType duration = new DurationType(1);
    						duration.whenToEnd = WhenToEnd.StartOfTurn;
    						DefaultModifierType mt = new DefaultModifierType(modifierName) {
    							private static final long serialVersionUID = 1L;
    							private boolean skippedFirst = false;
    							@Override
    							public int getClutchArmorClassBonus(Player me, Action action, Attack attack, Player target, GameState state, int usedRoll, int attackBonus, int armorClass, AttackSettings settings) {
    								if (skippedFirst) {
    									return 5;
    								} else {
    									skippedFirst = true;
    									return 0;
    								}
    							}
    						};
    						Modifier m = new Modifier(modifierName, mt, target, new Action(modifierName, RefList.actiontypesfreeaction), duration, state);
    						target.getStatus().getModifiers().add(m);
    						return 5;
    					}
    				}
    			}
    			return 0;
    		}
    	};
    }
    
    private Action coneOfCold() {
    	//TODO action name needs to be match spell name otherwise limited uses doesnt match
		Action a = new Action("Cone Of Cold", RefList.actiontypesmainaction);

		a.setMonsterSpell(new ConeOfCold());
		a.setLimitedUses(1);

		return a;
	}

    
    private Action fireball() {
    	//TODO action name needs to be match spell name otherwise limited uses doesnt match
		Action a = new Action("Fireball", RefList.actiontypesmainaction);

		a.setMonsterSpell(new Fireball());
		a.setLimitedUses(2);

		return a;
	}    
   

    private Attack ArcaneBurst() {
        Attack a = new Attack("Arcane Burst"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesforce); 
        return a;
    }
    

    private Attack ArcaneBurstRanged() {
        Attack a = new Attack("Arcane Burst"); 
        a.setAttackBonus(6) 
         .setAttackRange(120)
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesforce); 
        return a;
    }
    
    private Attack ArcaneBurstThrown() {
        Attack a = new Attack("Arcane Burst Thrown"); 
        a.setAttackBonus(6) 
         .setAttackRange(120) // 0 if not found
         .setLongRange(0) // 0 if not found
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesforce); 
        return a;
    }

}

