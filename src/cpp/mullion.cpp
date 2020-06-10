#include <iostream>
#include "jniMullion.h"

int main() {
    JavaCPP_init(0, NULL);
    try {
      mullion(1,2);
    } catch (std::exception &e) {
        std::cout << e.what() << std::endl;
    }
    JavaCPP_uninit();
}
