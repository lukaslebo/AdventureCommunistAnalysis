import {BEM} from './bem';

describe('BEM', () => {
  it('should create BEM classes', () => {
    const styles = BEM('BLOCK');
    expect(styles('ELEMENT')).toEqual('BLOCK__ELEMENT');
    expect(styles('ELEMENT', 'M1', 'M2')).toEqual(
      'BLOCK__ELEMENT BLOCK__ELEMENT--M1 BLOCK__ELEMENT--M2'
    );

    expect(styles(null, 'M1', 'M2')).toEqual('BLOCK BLOCK--M1 BLOCK--M2');
  });
});
