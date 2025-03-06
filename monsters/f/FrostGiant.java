/**
 * 
 */
package com.dndcombat.monsters.f;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Modifier;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.util.Roll;

/**
 
 */
public class FrostGiant extends Player {
 
	private static final long serialVersionUID = 1L;

	public FrostGiant() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Frost Giant");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Frost Giant.webp");

	    str(23).dex(9).con(21).inte(9).wis(10).cha(12);

		// Saving Throws
		strSave(+6).dexSave(-1).conSave(+8).intSave(-1).wisSave(+3).chaSave(+4);
	
		 // Combat Stats
		ac(15).init(+2).hp(149).speed(40).cr(8);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesgiant);

		damageImmune(RefList.damagetypescold);

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( FrostAxe(), FrostAxe() ));
        
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( GreatBow(), GreatBow() ));


        addAction(new Action("Frost Axe", RefList.actiontypesmainaction)
                .addAttack(FrostAxe()));
        addAction(new Action("Great Bow", RefList.actiontypesmainaction)
                .addAttack(GreatBow()));

        getCreature().getModifierTypes().add(WarCry());
	}


	private Attack FrostAxe() {
	
        Attack a = new Attack("Frost Axe"); 
        a.setAttackBonus(9) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(12) 
         .bonus(6) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.addDamage().nbrDice(2).dieType(8).damageTypeRefId(RefList.damagetypescold);
        
        return a;
    }
    private Attack GreatBow() {
        Attack a = new Attack("Great Bow"); 
        a.setAttackBonus(9) 
         .setAttackRange(150) 
         .setLongRange(600) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(10) 
         .bonus(6) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        a.addDamage().nbrDice(2).dieType(8).damageTypeRefId(RefList.damagetypescold);
        //TODO speed decreased
 
        return a;
    }

    private ModifierType WarCry() {
    	String name = "War Cry";
    	DefaultModifierType mt = new DefaultModifierType("War Cry") {
 
			private static final long serialVersionUID = 1L;
    	 
			@Override
			public void applyModifierTypeOnMeStartOfMyTurn(Player me, GameState state) {
				if (me.hasBonusAction()) {
					me.useBonusAction();
					int temp = Roll.ndplus(2, 10, 5).getTotal();
					if (temp > me.getStatus().getTemporaryHitPoints()) {
						me.getStatus().setTemporaryHitPoints(temp);
						state.addActionLog(me.getName() + " uses War Cry for " + temp + " temporary hit points");
						Modifier m = new Modifier(name, new DefaultModifierType(name) {
 
							private static final long serialVersionUID = 1L;
							public boolean statusModifierOnSourceAlterAdvantageRemoveIfTrue(Modifier m, com.dndcombat.fight.model.AdvantageModel advantage, Player source, Player target, GameState state) {
								advantage.setAdvantage(true);
								advantage.setAdvantageReason(name);
								return true;
							};
							
						}, me, new Action(name,RefList.actiontypesfreeaction), new DurationType(1), state);
						me.getStatus().addModifiers(m);
					}
				}
			}
    	};
    	return mt;
    }
}

