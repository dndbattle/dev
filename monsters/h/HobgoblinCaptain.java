/**
 * 
 */
package com.dndcombat.monsters.h;

import com.dndcombat.fight.map.DnDDistanceCalculator;
import com.dndcombat.fight.model.AdvantageModel;
import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class HobgoblinCaptain extends Player {
 
	private static final long serialVersionUID = 1L;

	public HobgoblinCaptain() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Hobgoblin Captain");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Hobgoblin Captain.webp");

	    str(15).dex(14).con(14).inte(12).wis(10).cha(13);

		// Saving Throws
		strSave(+2).dexSave(+2).conSave(+2).intSave(+1).wisSave(+0).chaSave(+1);
	
		 // Combat Stats
		ac(17).init(+4).hp(58).speed(30).cr(3);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesgoblinoid);


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Greatsword(), Greatsword()));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Longbow(), Longbow()));


        addAction(new Action("Greatsword", RefList.actiontypesmainaction)
                .addAttack(Greatsword()));
        addAction(new Action("Longbow", RefList.actiontypesmainaction)
                .addAttack(Longbow()));
        
        getCreature().addModifierType(aura());

	}


	private ModifierType aura() {
		return new DefaultModifierType("Aura of Authority") {
			 
			private static final long serialVersionUID = 1L;
		 
			@Override
			public void modifierTypeOnSomeoneAttackAlterAdvantage(Player someone, AdvantageModel a, Player source, Player target, Action action, Attack attack, GameState state, AttackSettings settings) {
				if (someone.isIncapacitated() || someone.isEnemy(source)) {
					return; // doesnt affect itself or non undead
				}
				if (DnDDistanceCalculator.roughDistanceInSquares(someone, source) > 2) {
					return;
				}
				a.setAdvantage(true);
				a.setAdvantageReason(modifierName);
			}
			
			@Override
			public void modifierTypeOnSomeoneAlterSaveAdvantage(Player someone, AdvantageModel a, Player source, Player meMakingSave, Action action, GameState state) {
				if (someone.isIncapacitated() || someone.isEnemy(source)) {
					return; // doesnt affect itself or non undead
				}
				if (DnDDistanceCalculator.roughDistanceInSquares(someone, source) > 2) {
					return;
				}
				a.setAdvantage(true);
				a.setAdvantageReason(modifierName);
			}
		};
	}

	private Attack Greatsword() {
	
        Attack a = new Attack("Greatsword"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack Longbow() {
        Attack a = new Attack("Longbow"); 
        a.setAttackBonus(4) 
         .setAttackRange(150) 
         .setLongRange(600) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        a.addDamage().nbrDice(2).dieType(4).damageTypeRefId(RefList.damagetypespoison);
        return a;
    }

}

