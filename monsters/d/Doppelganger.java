/**
 * 
 */
package com.dndcombat.monsters.d;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.AdvantageModel;
import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;

/**
 
 */
public class Doppelganger extends Player {
 
	private static final long serialVersionUID = 1L;

	public Doppelganger() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Doppelganger");
//  
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Doppelganger.webp");

	    str(11).dex(18).con(14).inte(11).wis(12).cha(14);

		// Saving Throws
		strSave(+0).dexSave(+4).conSave(+2).intSave(+0).wisSave(+1).chaSave(+2);
	
		 // Combat Stats
		ac(14).init(+4).hp(52).speed(30).cr(3);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesmonstrosity);

		conditionImmune(RefList.conditionscharmed);
	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Slam(), Slam(), unsettlingVisage() ));

        addAction(new Action("Slam", RefList.actiontypesmainaction)
                .addAttack( Slam() ));

        getCreature().addModifierType(new DefaultModifierType("Slam") {
 
			private static final long serialVersionUID = 1L;
			
			@Override
			public void modifierTypeOnSourceAlterAdvantage(AdvantageModel a, Player source, Player target, Action action, Attack attack, GameState state, AttackSettings settings) {
				if (state.getRound() == 1) {
					a.setAdvantage(true);
					a.setAdvantageReason("First round");
				}
			}
        	
        });
	}

    private Attack unsettlingVisage() {
		Attack a =new Attack();
		a.setAttackName("Unsettling Visage");
		SaveDC save = new SaveDC();
		save.dc = 12;
		save.saveVsRefId = RefList.savingthrowvswisdom;
		WithinXFeetEffect w = new WithinXFeetEffect(15, a.getAttackName(), Arrays.asList(new DamageModel().setFailConditionRefId(RefList.conditionsfrightened)),
				save, 1);
		w.recharge(6);
		a.setSpell(w);
		return a;
	}

	private Attack Slam() {
	
        Attack a = new Attack("Slam"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
}

