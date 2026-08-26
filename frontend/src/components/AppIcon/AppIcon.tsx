import FontAwesome6 from 'react-native-vector-icons/FontAwesome6';

type AppIconProps = {
  name: string;
  size?: number;
  color?: string;
};

function AppIcon({ name, size = 24, color = '#000' }: AppIconProps) {
  return <FontAwesome6 name={name} size={size} color={color} />;
}

export default AppIcon;