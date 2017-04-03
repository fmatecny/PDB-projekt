/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  ferino
 * Created: 12.12.2016
 */

create or replace PROCEDURE rotate (image_id NUMBER) AS
  image ORDSYS.ORDIMAGE;
BEGIN
  SELECT foto INTO image FROM fotografie WHERE id = image_id FOR UPDATE;
  ORDSYS.ORDIMAGE.process(image,'rotate=90');

EXCEPTION
  WHEN OTHERS THEN
   RAISE;
END;
/

COMMIT;
;;