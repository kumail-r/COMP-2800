#include <stdio.h>
#include <stdlib.h>

int main() {
    char header[] = {'P','6','\n',
                     '1','0','0','0',' ','1','0','0','0','\n',
                     '2','5','5','\n'};
    FILE *pImage = fopen("image.ppm", "wb");
    fwrite(header, 1, sizeof(header)/sizeof(header[0]), pImage);
    fclose(pImage);

    char middleColor[] = {255, 255, 255};
    int colorSize = sizeof(middleColor)/sizeof(middleColor[0]);
    
    pImage = fopen("image.ppm", "ab");
    for (int i = 0; i < 1000; i++){
        int width = 0;
        if (i >= 250 && i <= 750){
            width = (-1)*abs(i-500)+251;
        }
        for (int j = 0; j < 1000; j++){
            if (i < 500){
                if (j < 500){
                    if (j <= 500 - width) fwrite((char[]){255,0,0}, 1, colorSize, pImage);
                    else fwrite(middleColor, 1, colorSize, pImage);
                }
                else{
                    if (j >= 500 + width) fwrite((char[]){0,255,0}, 1, colorSize, pImage);
                    else fwrite(middleColor, 1, colorSize, pImage);
                }
            }
            else{
                if (j < 500){
                    if (j <= 500 - width) fwrite((char[]){0,0,255}, 1, colorSize, pImage);
                    else fwrite(middleColor, 1, colorSize, pImage);
                }
                else{
                    if (j >= 500 + width) fwrite((char[]){0,0,0}, 1, colorSize, pImage);
                    else fwrite(middleColor, 1, colorSize, pImage);
                }
            }
        }
    }
    fclose(pImage);
    printf("image.ppm created");
    return 0;
}