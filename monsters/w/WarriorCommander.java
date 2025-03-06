/**
 * 
 */
package com.dndcombat.monsters.w;


import com.dndcombat.actions.ActionManager;
import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.fight.modifiertypes.SapWeaponMastery;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class WarriorCommander extends Player {
 
	private static final long serialVersionUID = 1L;

	public WarriorCommander() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Warrior Commander");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Veteran.webp");

	    str(21).dex(20).con(18).inte(14).wis(16).cha(14);

		// Saving Throws
		strSave(+9).dexSave(+9).conSave(+8).intSave(+2).wisSave(+7).chaSave(+2);
	
		 // Combat Stats
		ac(18).init(+9).hp(161).speed(30).cr(10);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Greatsword(), Greatsword(), Greatsword() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Longbow(), Longbow(), Longbow() ));

        addAction(new Action("Greatsword", RefList.actiontypesmainaction)
                .addAttack(Greatsword()));
        addAction(new Action("Longbow", RefList.actiontypesmainaction)
                .addAttack(Longbow()));
        getCreature().getModifierTypes().add(counterattack());

	}
    

    private ModifierType counterattack() {
		return new DefaultModifierType("Counterattack") {
 
			private static final long serialVersionUID = 1L;
			 
			@Override
			public int getClutchArmorClassBonus(Player me, Action a, Attack attack, Player target, GameState state, int usedRoll, int attackBonus, int armorClass, AttackSettings settings) {
				int acbonus = 4;
				if (target.hasReaction() && attack.isMeleeAttack()) {
					if (usedRoll + attackBonus >= armorClass 
							&& usedRoll + attackBonus -acbonus < armorClass) {
						state.addActionLog(target.getName() + " uses parry +"+acbonus+" AC");
						target.useReaction();
						target.markCast(modifierName);
				        Action action = new Action("Greatsword", RefList.actiontypesmainaction)
				                .addAttack(Greatsword());
				        if (!me.isNextTo(target)) {
				        	action = new Action("Longbow", RefList.actiontypesmainaction)
				                    .addAttack(Longbow());
				        }
				        
						state.addActionLog(target.getName() + " reacts with "+modifierName + " on " + me.getName(), state);
						new ActionManager().rollToHit(target, action, action.getFirstAttack(), me, state, new AttackSettings());
						return acbonus;
					}
				}
				return 0;
			}
		};
	}

	//TODO maneuver, tactical charge
	private Attack Greatsword() {
	
        Attack a = new Attack("Greatsword"); 
        a.setAttackBonus(9) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(6) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.getModifierTypes().add(new SapWeaponMastery("Sap", new DurationType(0)));
        
        return a;
    }
    private Attack Longbow() {
        Attack a = new Attack("Longbow"); 
        a.setAttackBonus(9) 
         .setAttackRange(150) 
         .setLongRange(600) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(8) 
         .bonus(5) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        a.getModifierTypes().add(new SapWeaponMastery("Sap", new DurationType(0)));
 
        return a;
    }

}

